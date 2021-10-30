#!/bin/bash
#for FILE in simsettings/;
#  do
#    echo $FILE
  #for i in {0..9}
#do
  #java -jar PureEdgeSim.jar $FILE ./output
#donejava -jar PureEdgeSim.jar $FILE ./output/200/
#done

for FILE in simsettings/*; do
    if [ -d "$FILE" ]; then
        java -jar PureEdgeSim.jar "${FILE}/" ./output/
    fi
done
