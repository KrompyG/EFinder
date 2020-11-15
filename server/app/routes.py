from app import app
from flask import request
from werkzeug.utils import secure_filename
import io
import os
# Imports the Google Cloud client library
from google.cloud import vision

# check file's extension
def allowed_file(filename):
    return ('.' in filename and
            filename.rsplit('.', 1)[1] in app.config['ALLOWED_EXTENSIONS'])


@app.route('/')
@app.route('/index')
def index():
    return "Hello, this is Efinder!"


@app.route('/upload', methods=['POST'])
def upload():
    # getting image file from request
    imageFile = request.files['imagefile']

    # checking file existance and its extension
    if imageFile and allowed_file(imageFile.filename):
        # Instantiates a client
        client = vision.ImageAnnotatorClient()
        # creating Image for sending it to Google
        image = vision.Image(content=imageFile.read())
        # Performs label detection on the image file
        # pylint: disable=no-member
        response = client.text_detection(image=image,
            image_context={"language_hints": ["ru"]})
        # getting recognised text from response
        texts  = response.text_annotations

        # texts[0] - весь распознанный текст целиком
        # начиная с texts[1] - отдельные слова
        for text in texts:
            print('\n"{}"'.format(text.description))

        # TODO: write here code that finds E additives
        # and send them back to client
        return "image received successfully\n"
    
    return "failed to get an image"
