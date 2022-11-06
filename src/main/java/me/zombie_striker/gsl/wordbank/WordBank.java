package me.zombie_striker.gsl.wordbank;

import me.zombie_striker.gsl.files.YamlParserLoader;
import me.zombie_striker.gsl.utils.FileUtils;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class WordBank {

    private static List<String> adjectives;
    private static List<String> nouns;
    private static List<String> sentences;

    public static final String REPLACE_NOUN = "%noun%";
    public static final String REPLACE_ADJECTIVVE = "%adjective%";

    public static void init() {
        YamlParserLoader parserLoader = new YamlParserLoader()
                .addDefault("adjectives", Arrays.asList("Cute","Cruel","Caring","Kind","Stupid","Red","Blue","Minted","Gray","White","Black","Brown","Communist","Capitalist","Apolitical","Political","Determined","Dead"))
                .addDefault("nouns", Arrays.asList("Cat","Gato","Anarchist","Capitalist","Communist","Fascist","City","Gulag","Prison","Player","Hat","Helmet","Enderman","Prison Pearl","Creeper","Man","Machine","Zombie","Pig","Determination","Cow","Horse","Skeleton","Skull"))
                .addDefault("sentences", Arrays.asList(
                        REPLACE_NOUN,
                        REPLACE_ADJECTIVVE+" "+REPLACE_NOUN,

                        REPLACE_ADJECTIVVE+" "+REPLACE_NOUN+" and "+REPLACE_ADJECTIVVE+" "+REPLACE_NOUN,
                        REPLACE_ADJECTIVVE+" "+REPLACE_NOUN+" and "+REPLACE_NOUN,
                        REPLACE_NOUN+" and "+REPLACE_ADJECTIVVE+" "+REPLACE_NOUN
                ));

        File f = FileUtils.getFolder(FileUtils.PATH_WORDBANK_FILE);
        if(!f.exists()) {
            try {
                f.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        FileConfiguration fc = YamlConfiguration.loadConfiguration(f);
        if (!parserLoader.verifyAllPathsAreThere(fc))
            parserLoader.addDefaultValues(fc, f);

        adjectives = fc.getStringList("adjectives");
        nouns = fc.getStringList("nouns");
        sentences = fc.getStringList("sentences");
    }
    public static String getRandomWordbank(int key1, int key2){
        int psudorandom = key1+(key2*key1);
        String sentence = getRandomSentece(psudorandom++);
        while(true){
            if(sentence.contains(REPLACE_NOUN)){
                sentence= sentence.replaceFirst(REPLACE_NOUN,getRandomNoun(psudorandom++));
                continue;
            }
            if(sentence.contains(REPLACE_ADJECTIVVE)){
                sentence = sentence.replaceFirst(REPLACE_ADJECTIVVE,getRandomAdjective(psudorandom++));
                continue;
            }
            break;
        }
        return sentence;
    }

    public static String getRandomSentece(int key){
        Random random1 = new Random(key);
        String sentence = sentences.get(random1.nextInt(sentences.size()));
       return sentence;
    }
    public static String getRandomAdjective(int key){
        Random random1 = new Random(key);
        String sentence = adjectives.get(random1.nextInt(adjectives.size()));
        return sentence;
    }
    public static String getRandomNoun(int key){
        Random random1 = new Random(key);
        String noun = nouns.get(random1.nextInt(nouns.size()));
        return noun;
    }
}
