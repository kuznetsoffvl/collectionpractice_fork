package model;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class TestCollections {



    @Test
    void testPrintList() {
        //todo Распечатать содержимое используя for each
        for (HeavyBox box : list) {
            System.out.println(box);
        }
    }

    @Test @Disabled
    void testChangeWeightOfFirstByOne() {
        //todo Изменить вес первой коробки на 1.
        HeavyBox heavyBox = list.get(0);
        heavyBox.setWeight(heavyBox.getWeight() + 1);

        assertEquals(new HeavyBox(1,2,3,5), list.get(0));
        assertEquals(new HeavyBox(3,3,3,4), list.get(1));
    }

    @Test @Disabled
    void testDeleteBeforeLast() {
        //todo Удалить предпоследнюю коробку.
        list.remove(list.size() - 2);
        assertEquals(6, list.size());
        assertEquals(new HeavyBox(1,2,3,4), list.get(0));
        assertEquals(new HeavyBox(1,3,3,4), list.get(list.size()-2));
        assertEquals(new HeavyBox(1,1,1,1), list.get(list.size()-1));
    }

    @Test @Disabled
    void testConvertToArray() {
        //todo Получить массив содержащий коробки из коллекции тремя способами и вывести на консоль.
        HeavyBox[] arr = list.toArray(new HeavyBox[list.size()]);
        //HeavyBox[] arr = null;
        assertArrayEquals(new HeavyBox[]{
                new HeavyBox(1,2,3,4),
                new HeavyBox(3,3,3,4),
                new HeavyBox(2,6,5,3),
                new HeavyBox(2,3,4,7),
                new HeavyBox(1,3,3,4),
                new HeavyBox(1,2,3,4),
                new HeavyBox(1,1,1,1)
        }, arr);
    }

    @Test @Disabled
    void testDeleteBoxesByWeight() {
        // todo удалить все коробки, которые весят 4
        for (Iterator<HeavyBox> iterator = list.iterator(); iterator.hasNext(); ) {
            HeavyBox box =  iterator.next();
            if (box.getWeight() == 4){
                iterator.remove();
            }
        }
        assertEquals(3, list.size());
    }

    @Test @Disabled
    void testSortBoxesByWeight() {
        // отсортировать коробки по возрастанию веса. При одинаковом весе - по возрастанию объема
        Collections.sort(list, new Comparator<HeavyBox>() {
            @Override
            public int compare(HeavyBox o1, HeavyBox o2) {
                if (o1.getWeight() < o2.getWeight()) return -1;
                if (o1.getWeight() > o2.getWeight()) return +1;
                return Double.compare(o1.getVolume(), o2.getVolume());
            }
        });
        assertEquals(new HeavyBox(1,1,1,1), list.get(0));
        assertEquals(new HeavyBox(2,3,4,7), list.get(6));
        assertEquals(new HeavyBox(1,2,3,4), list.get(3));
        assertEquals(new HeavyBox(1,3,3,4), list.get(4));
    }

    @Test @Disabled
    void testClearList() {
        //todo Удалить все коробки.
        List<HeavyBox> tmp = list;
        list.clear();
        //list = new ArrayList<>();
        assertTrue(list.isEmpty());
        assertEquals(tmp, list);
    }

    @Test @Disabled
    void testReadAllLinesFromFileToList() {
        // todo Прочитать все строки в коллекцию

        List<String> lines = new ArrayList<>();
        String line;
        while (true) {
            try {
                if (!((line = reader.readLine())!=null)) break;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            lines.add(line);
        }
        assertEquals(19, lines.size());
        assertEquals("", lines.get(8));
    }

    @Test @Disabled
    void testReadAllWordsFromFileToList() throws IOException {
        // todo прочитать все строки, разбить на слова и записать в коллекцию
        List<String> words = readAllWordsFromFileToList();
        assertEquals(257, words.size());
    }

    List<String> readAllWordsFromFileToList() throws IOException {

        List<String> result = new ArrayList<>();
        String line;

        while ((line = reader.readLine()) != null) {

            if (line.isBlank()) {continue;}

            String[] strings = line.split(REGEXP);
            result.addAll(List.of(strings));
            //System.out.println(line);
        }

        return result;
    }

    @Test @Disabled
    void testFindLongestWord() {
        // todo Найти самое длинное слово


        assertEquals("conversations", findLongestWord());
    }

    private String findLongestWord() {
        List<String> srtrings = null;
        try {
            srtrings = readAllWordsFromFileToList();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        String longest = "";
        int max = 0;
        for (String string : srtrings) {
            if (string.length() > longest.length())
                longest = string;
        }
        return longest;
    }

    @Test @Disabled
    void testAllWordsByAlphabetWithoutRepeat() throws IOException {
        // todo Получить список всех слов в нижнем регистре по алфавиту без повторов
        List<String> strings = readAllWordsFromFileToList();
        for (int i = 0; i < strings.size(); i++) {
            String s = strings.get(i);
            strings.set(i, s.toLowerCase());
        }
        Set<String> set = new TreeSet<>(strings);
        List<String> result = new ArrayList<>(set);

        assertEquals("alice", result.get(5));
        assertEquals("all", result.get(6));
        assertEquals("without", result.get(134));
        assertEquals(138, result.size());
    }

    @Test @Disabled
    void testFindMostFrequentWord() throws IOException {
        // todo Найти самое часто вчтречающееся слово
        assertEquals("the", mostFrequentWord());
    }

    private String mostFrequentWord() throws IOException {
        List<String> strings = readAllWordsFromFileToList();
        for (int i = 0; i < strings.size(); i++) {
            String s = strings.get(i);
            strings.set(i, s.toLowerCase());
        }
        Map<String, Integer> map = new HashMap<>();
        for (String s : strings) {
            Integer k = map.get(s);
            map.put(s, k==null ? 1 : k+1);
            //map.merge(s, 1, Integer::sum);
        }
        int max = 0;
        String result = null;
        for (Map.Entry <String, Integer> entry : map.entrySet()) {
            if (entry.getValue() > max) {
                max = entry.getValue();
                result = entry.getKey();
            }
        }
        return result;
    }

    @Test @Disabled
    void testFindLessFiveSymbolsWords() throws IOException {
        // todo Отобрать в отдельный список слова, длина которых меньше 5 (в нижнем регистре, без повторов, по алфавиту) - в конце тестов
        List <String> list = FindLessFiveSymbolsWords();

        System.out.println(list.get(1));
        assertEquals("all", list.get(1));
        assertEquals("and", list.get(2));
        assertEquals("as", list.get(3));
        assertEquals("at", list.get(4));
    }

    private List<String> FindLessFiveSymbolsWords() throws IOException {

        List<String> list = new ArrayList<>();
        List<String> strings = readAllWordsFromFileToList();

        for (int i = 0; i < strings.size(); i++) {
            String s = strings.get(i);
            if (s.length() < 5) {
                list.add(s.toLowerCase());
            }
        }
        Set<String> set = new TreeSet<>(list);
        List<String> result = new ArrayList<>(set);

        System.out.println(result);
        return result;
    }

    List<HeavyBox> list;

    @BeforeEach
    void setUp() {
        list = new ArrayList<>(List.of(
                new HeavyBox(1,2,3,4),
                new HeavyBox(3,3,3,4),
                new HeavyBox(2,6,5,3),
                new HeavyBox(2,3,4,7),
                new HeavyBox(1,3,3,4),
                new HeavyBox(1,2,3,4),
                new HeavyBox(1,1,1,1)
        ));
    }

    static final String REGEXP = "\\W+"; // for splitting into words

    private BufferedReader reader;

    @BeforeEach
    public void setUpBufferedReader() throws IOException {
        reader = Files.newBufferedReader(
                Paths.get("Text.txt"), StandardCharsets.UTF_8);
    }

    @AfterEach
    public void closeBufferedReader() throws IOException {
        reader.close();
    }
}
