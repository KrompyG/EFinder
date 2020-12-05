from app import app
from flask import request, jsonify
from werkzeug.utils import secure_filename
import io
import os
import re
# Imports the Google Cloud client library
from google.cloud import vision

# check file's extension
def allowed_file(filename):
    return ('.' in filename and
            filename.rsplit('.', 1)[1] in app.config['ALLOWED_EXTENSIONS'])

def find_dopings(composition):
    regex = "[eEеЕ]{1,1}[-]?[0-9]{1,4}" # условие для поиска добавок
    return re.findall(regex, composition)

@app.route('/')
@app.route('/index')
def index():
    return "Hello, this is Efinder!"


@app.route('/json')
def show_json():
    my_list = []
    text = "this is very long text"
    return jsonify(text=text, list=my_list)


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
        composition = texts[0].description
        print(composition)
        dopings = find_dopings(composition)
        
        # приведение списка добавок к одному формату
        formatted_dopings = list(map(lambda doping:
            re.sub("[eEеЕ]{1,1}[-]?","e",doping), dopings))

        for doping in formatted_dopings:
            print('\n"{}"'.format(doping))

        # TODO: write here code that finds E additives
        # and send them back to client
        return jsonify(composition=composition,dopings=formatted_dopings)
    
    return jsonify(composition="failed to get an image",dopings=[])
