#!/bin/bash

java -jar target/rhyming-dict-0.0.1-SNAPSHOT.jar | tee run-log.txt

read -p "Exit? " answer

