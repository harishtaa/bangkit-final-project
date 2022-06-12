## About The Project

This is a project to fulfill the Bangkit Academy led by Google, Goto, and Traveloka Program. This app facilitates and identifies owners in detail about dog breeds, ranging from characteristics, types of food, diseases that usually attack certain types/breeds of animals and many others.

## Machine Learning Documentation

Link to Colab: <br>
https://colab.research.google.com/drive/1457QmwqTnaYD9ganvVHoKJ0nl7NidpLs

- ## 1. Load Datasets 
  - Load the modified dataset that we host to Google Drive, here is the link: <br> https://drive.google.com/drive/folders/1hMuDgvtpzym6CqARviPEekGIXePALmsz?usp=sharing

- ## 2. Pre-processing Datasets
  - Adding Augmentation to produce more images:
    - rotation_range=40
    - width_shift_range=0.2,
    - height_shift_range=0.2,
    - shear_range=0.2,
    - zoom_range=0.2,
    - horizontal_flip=True,
    - fill_mode='nearest'
  - Resizing the datasets into 300 x 300

- ## 3. Training

   - Using Transfer Learning with `Inception` to get better accuracy.
   - Using `CategoricalCrossentropy` as a loss parameter
   - Using `Adamax(learning_rate=0.001)` as an optimizer parameter
   - Added more layer too to `model.Sequential`:  
     -  Added `Flatten` layer
     -  Added `Dense(units=1024, activation='relu')` layer 
     -  Added `Dropout(units=0.5)` layer
     -  Added output layer `Dense(units=10, activation='softmax')`
  - Training with 50 epochs
  - Set the `Callbacks` to stop training when accuracy reached 94%
  - From the result, it got:
    - `loss: 10.62%`
    - `accuracy: 96.75%`
    - `val_loss: 18.53%`
    - `val_accuracy: 94.33%`
- ## 4. Save the Model to Google Drive
  - Then, save the model (*.h5 format) to Google Drive (saved only the best model to Google Drive):<br>
https://drive.google.com/file/d/11LvSzaB2yWfIFTM9it8zXaMbNGnM3Srd/view?usp=sharing

## Mobile Development Documentation
- ## 1. Feature
   - Splash Screen, before entering the application there is a splashscreen shows app logo
   - Pick image from galery, user can pick image from galery
   - Capture image from camera, user can capture image from camera realtime
   - Send image to cloud, user can send image to cloud to predict their dog image
   - Get the details of the dog, user can see the details of their dog, include Activity Level, Barking Level, Common Healt Problem, etc.
- ## 2. Build With
   - Android Studio
   - Kotlin
   - Retrofit
