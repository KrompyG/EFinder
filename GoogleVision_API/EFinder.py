import io
import os

# Imports the Google Cloud client library
from google.cloud import vision

os.environ['GOOGLE_APPLICATION_CREDENTIALS'] = r'key.json'

# Instantiates a client
client = vision.ImageAnnotatorClient()

# The name of the image file to annotate
file_name = os.path.abspath('resources/demo-img1.jpg')

# Loads the image into memory
with io.open(file_name, 'rb') as image_file:
    content = image_file.read()

image = vision.Image(content=content)

# Performs label detection on the image file
# pylint: disable=no-member
response = client.text_detection(image=image,
    image_context={"language_hints": ["ru"]})
texts  = response.text_annotations

for text in texts:
        print('\n"{}"'.format(text.description))
