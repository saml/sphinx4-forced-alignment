import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;

import edu.cmu.sphinx.frontend.util.AudioFileDataSource;
import edu.cmu.sphinx.linguist.language.grammar.TextAlignerGrammar;
import edu.cmu.sphinx.recognizer.Recognizer;
import edu.cmu.sphinx.result.Result;
import edu.cmu.sphinx.util.props.ConfigurationManager;


public class ForcedAlignment {
    private final String configPath;
    private final String audioPath;
    private final String textPath;
    
    
    public ForcedAlignment() {
        configPath = System.getProperty("config", null);
        audioPath = System.getProperty("audio", null);
        textPath = System.getProperty("text", null);
    }
    
    private static String readFile(String path) throws IOException {
        final FileInputStream stream = new FileInputStream(new File(path));
        try {
            final FileChannel channel = stream.getChannel();
            final MappedByteBuffer buffer = channel.map(FileChannel.MapMode.READ_ONLY, 0, channel.size());
            return Charset.defaultCharset().decode(buffer).toString();
        } finally {
            stream.close();
        }
    }
    
    private void usage() {
        System.out.format("Usage: java -Dconfig=config/aligner.xml -Daudio=data/audio.wav -Dtext=data/audio.txt -jar %s.jar\n", this.getClass().getName());
    }
    
    private void error() {
        System.exit(1);
    }
    
    private void start() throws IOException {
        if (configPath == null || audioPath == null || textPath == null) {
            usage();
            error();
        }

        final String text = readFile(textPath).replaceAll("[^\\w']+", " ");
        
        final ConfigurationManager cm = new ConfigurationManager(configPath);
        final Recognizer recognizer = (Recognizer) cm.lookup("recognizer");
        final TextAlignerGrammar grammar = (TextAlignerGrammar) cm.lookup("textAlignGrammar");
        grammar.setText(text);
        recognizer.addResultListener(grammar);
        recognizer.allocate();
        final AudioFileDataSource dataSource = (AudioFileDataSource) cm.lookup("audioFileDataSource");
        dataSource.setAudioFile(new File(audioPath), null);
        
        Result result;
        while ((result = recognizer.recognize()) != null) {
            final String resultText = result.getTimedBestResult(false, true);
            System.out.println(resultText);
        }
        
    }
    public static void main(String[] args) throws IOException {
        new ForcedAlignment().start();
    }
}
