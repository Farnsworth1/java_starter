#! /bin/sh

find ./tests -name "E[0-9][0-9][0-9].txt" > liste.txt
 
for TEST in `cat liste.txt`

do java -jar ./out/artifacts/Vorlage/Vorlage.jar -i $TEST

done 
rm -rf liste.txt