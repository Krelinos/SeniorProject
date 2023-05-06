# Microchip XML Graph Application Instruction Manual

This application was created using Apache NetBeans IDE 17 and JDK 20. It is likely that these versions will be required to compile this application.
Additionally, this project uses the OpenJavaFX library, which can be downloaded at https://openjfx.io/

Once you have successfully compiled the application, the application window will appear.

### Import graphs

This is where an XML file can be imported to be analyzed and displayed onto the graph. Multiple XML files can be selected by holding Control while left clicking each XML file, or by box selection.

+ Click ‘File’ to select ‘Import Xml’. A file chooser window will appear that only displays XML file(s).


### Adjusting the line graphs

The XML file(s) is/are now located on the left hand side, where they appear as a list of layers. The order in which the layers are shown is also the priority at which they are displayed on the line chart, whereas the top most layer is displayed first.

+ Click on the up and down arrow keys to change the order in which that waveform is displayed. Additionally, holding control then clicking on an arrow will bring the layer directly to the top or the bottom.



### Changing the color of the line graphs
The color of individual line charts can be freely changed. All the graphs are imported as the same color, denoted by the default waveform color option in the settings.

+ Click on the box with the black square. A color picker pop-up will be displayed, then select the desired waveform color. 

+ To create a custom color that is not shown in the picker, click ‘Custom color…’ to create a custom color for the line graph.




### Make graphs visible/invisible
This will make a line graph invisible or visible again, without deleting it from the list of existing graphs. By default all graphs are made visible with a check marked box on the right hand side of the graph layer.

+ Click on the rightmost box with the checkmark inside it to make it invisible, click again to make it visible again.

+ If the line graph is not visible from the beginning, the box will instead be empty, click the box to make it visible again.

+ Additionally, if control is held while left clicking a waveform layer, all layers will be hidden except for that layer.

### Removing line graphs
Remove line graphs from the list that are no longer needed.

+ Click the ‘X’ button on the left hand side of the line graph layer to delete the graph from the list of graphs.

+ Additionally, if control is held while left clicking the remove button, all other layers will be deleted except for that layer.


### Naming line graphs
Each line graph can be given its own name, so that it is easier to differentiate between them. By default, each imported waveform has the first eight characters of their unique ID within the text box, ‘Waveform Name’.

+ Click on the text box in the middle of the line graph layer and enter the desired name of the line graph.

+ Additionally, selecting this text box will result in that layer becoming the layer to have its information displayed. The top right of the graph and the table on the bottom half of the application will fill with information regarding that layer.

### How to export
An export option is available to export the graph into a JPG image or CSV file for Excel, containing each waveform's time (in nanoseconds) and the current at that time (in amplitude).

+ Click on ‘Export’ then either ‘As Image’ or 'As Image (without point info). The File Explorer(Windows) / Finder(Mac) will pop-up, where the desired location and name of the file can be set.

+ To export data as an CSV file, click ‘Export’ then ‘CSV’. The File Explorer(Windows) / Finder(Mac) will pop-up, where the desired location and name of the file can be set.



### Changing the background & default waveform color
By default the graph background color is white, but this can be changed to help certain line graph colors stand out against the background.

+ Click ‘Settings’ and ‘Fonts & Colors’, a pop-up window will appear with two drop-down menus.

+ Click on ‘Graph Background Color’, select any color provided or add a custom color by clicking ‘Custom Color…’. The graph color will change when either 'Apply & Close' or 'Apply' is clicked.

+ To change the default waveform color, click on ‘Default Waveform Color’. Select any color provided or add a custom color to be applied to future imported waveforms.

