# About

Modification of Aligner.java from sphinx4 so that audio and transcription text can be specified on command line.

# Steps:

    ant -Dsphinx4=/path/to/sphinx4
    java -Dconfig=config/aligner.xml -Daudio=/path/to/audio.wav -Dtext=/path/to/transcription.txt -jar ForcedAlignment.jar

# Install sphinx4:

tested with [sphinx4-1.0beta5](http://sourceforge.net/projects/cmusphinx/files/sphinx4/1.0%20beta5/sphinx4-1.0beta5-bin.zip/download)

Download it and unzip to /some/path. Let's call it $SPHINX4_HOME.

# Build using Ant:

    ant -Dsphinx4=$SPHINX4_HOME

it'll create ForcedAlignment.jar

# Run with audio and text

    java -Dconfig=config/aligner.xml -Daudio=youraudio.wav -Dtext=yourtranscription.txt -jar ForcedAlignment.jar

output will be "word(start-time,end-time)".
For example, if transcription file contained "hello, world!", output would be:

    hello(0.1,0.5) world(0.6,0.9)




