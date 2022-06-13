import base64
import os
os.environ['TF_CPP_MIN_LOG_LEVEL'] = '2'
import PIL.Image as Image 
import io
import tensorflow as tf 
import numpy as np
import yaml
import pymysql
import json

from tensorflow import keras 
from tensorflow.keras.preprocessing.image import img_to_array
from PIL import Image
from flask import Flask, request, jsonify

app = Flask(__name__)

class create_dict(dict): 
  
    # __init__ function 
    def __init__(self): 
        self = dict() 
          
    # Function to add key:value 
    def add(self, key, value): 
        self[key] = value

#pymysql init
db = yaml.safe_load(open('db.yaml'))
con = pymysql.connect(
   host         = db['mysql_host'],
   port         = db['mysql_port'],
   user         = db['mysql_user'],
   password     = db['mysql_password'],
   database     = db['mysql_db']
)

model = keras.models.load_model("IncDogBreed.h5") #load model

def base64conv(img64): #converts base64 
    img64con = base64.b64decode(img64)
    byteImgIO = io.BytesIO()
    img64fin = Image.open(io.BytesIO(img64con))
    img64fin.save(byteImgIO, "png")
    byteImgIO.seek(0)
    img64fin = byteImgIO.read()
    return img64fin



def transform_image(pillow_image):    
    data = []
    img = pillow_image.resize((300,300))
    img = img_to_array(img)
    img = img.astype(np.float32) / 255
    data.append(img)
    data = tf.image.resize(img, [300,300])
    data = np.expand_dims(data, axis=0)
    
    return data

def predict(x):
    list = ['"Beagle" ', '"Bull Mastiff"', ' "Chihuahua" ', ' "German Shepherd" ', '"Golden Retriever"', '"Maltese"', '"Pomeranian"', ' "Pug" ', '"Shih Tzu"', ' "Siberian Husky" ']
    predictions = model(x)
    predictions = tf.nn.softmax(predictions)
    pred0 = predictions[0]
    label0 = list[np.argmax(pred0)]
    return label0

@app.route("/", methods=["GET"])
def alldogs():
    db = yaml.safe_load(open('db.yaml'))
    con = pymysql.connect(
         host         = db['mysql_host'],
         port         = db['mysql_port'],
         user         = db['mysql_user'],
         password     = db['mysql_password'],
         database     = db['mysql_db']
)
    if request.method == "GET":
        with con:
            cur = con.cursor()
            dogs_all = cur.execute("SELECT * FROM dogdata")
    if dogs_all > 0 :
        all_dogs =  cur.fetchall()
        print(type(all_dogs))
    return jsonify(all_dogs) #is returning tuple not json *need fix

@app.route("/predict", methods=["POST"])
def predict_dog():
    db = yaml.safe_load(open('db.yaml'))
    con = pymysql.connect(
         host         = db['mysql_host'],
         port         = db['mysql_port'],
         user         = db['mysql_user'],
         password     = db['mysql_password'],
         database     = db['mysql_db']
)
    if request.method == "POST":
        incoming_jsondata = request.get_json()
        file = incoming_jsondata['data64']

        try:
            file2 = base64conv(file)
            pillow_img = Image.open(io.BytesIO(file2),)
            tensor = transform_image(pillow_img)
            prediction = predict(tensor)
            #query
            with con:
                cur = con.cursor()
                dog_prediction = cur.execute(f"SELECT * FROM dogdata WHERE dogname ={prediction}")
            if dog_prediction > 0 :
                mydict1 = create_dict()
                dogdetail = cur.fetchall()
                for row in dogdetail:
                 mydict1.add("dogdata:",({"pict":row[1],"breed":row[2],"activity":row[3],"barking":row[4],"train":row[5],"protec":row[6],"chp":row[7]}))
                 stud_json1 =  json.dumps(mydict1, indent=2, sort_keys=True)
                return (stud_json1)
            else:
                return "unknown"
        except Exception as e:
            return jsonify({"error": str(e)})
    return "OK"

@app.route("/<dogid>", methods=["GET"])
def spec(dogid):
    db = yaml.safe_load(open('db.yaml'))
    con = pymysql.connect(
         host         = db['mysql_host'],
         port         = db['mysql_port'],
         user         = db['mysql_user'],
         password     = db['mysql_password'],
         database     = db['mysql_db']
    )
    if request.method == "GET":
        with con:
            cur = con.cursor()
            dog_spec = cur.execute(f"SELECT * FROM dogdata WHERE dogname = {dogid}")
        if dog_spec > 0 :
            mydict = create_dict()
            dogdetail_id =  cur.fetchall()
            for row in dogdetail_id:
                 mydict.add("dogdata:",({"pict":row[1],"breed":row[2],"activity":row[3],"barking":row[4],"train":row[5],"protec":row[6],"chp":row[7]}))
                 stud_json =  json.dumps(mydict, indent=2, sort_keys=True)
        return (stud_json)

if __name__ == "__main__":
    app.run(debug=True, host="0.0.0.0", port=int(os.environ.get("PORT", 8080))) 

