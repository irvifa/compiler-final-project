#!/bin/bash

mv java_cup_v10k nyan
mv nyan/java_cup .
./run
cp -r java_cup modelLangProcInJava/
cd modelLangProcInJava
javac *.java

