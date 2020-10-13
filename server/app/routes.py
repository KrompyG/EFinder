from app import app
from flask import request
from werkzeug.utils import secure_filename
import os.path


# checking file's extension
def allowed_file(filename):
    return ('.' in filename and
            filename.rsplit('.', 1)[1] in app.config['ALLOWED_EXTENSIONS'])


@app.route('/')
@app.route('/index')
def index():
    return "Hello, World!"


@app.route('/upload', methods=['POST'])
def upload():
    imageFile = request.files.get('imagefile', '') 
    if imageFile and allowed_file(imageFile.filename):
        filename = secure_filename(imageFile.filename)

        # place here code for sending photo to Google API

        imageFile.save(os.path.join(app.config['UPLOAD_FOLDER'], filename))
        return "image received successfully"
    
    return "failed to get an image"
