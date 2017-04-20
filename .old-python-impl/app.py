#!/usr/bin/python

from flask import Flask, render_template
app = Flask(__name__)

@app.route("/")
def index():
    return "Hello World!"

@app.route('/hello/')
@app.route('/hello/<name>')
def hello(name=None):
        return render_template('hello.html', name=name)

@app.route('/word/<word>/num-matching-chars/<numchars>')
def getWordsPage(word, numchars):
    return render_template('words.html', word=word, numchars=numchars)


if __name__ == "__main__":
    app.run()
