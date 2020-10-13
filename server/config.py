import os

class Config(object):
    SECRET_KEY = os.environ.get('SECRET_KEY') or 'strong-secret-key-here'
    ALLOWED_EXTENSIONS = set(['txt', 'pdf', 'png', 'jpg', 'jpeg', 'gif'])
    UPLOAD_FOLDER = '../img/'