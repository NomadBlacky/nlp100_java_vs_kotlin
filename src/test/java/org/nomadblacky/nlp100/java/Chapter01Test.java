package org.nomadblacky.nlp100.java;

import org.junit.Test;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

/**
 * 自然言語処理100本ノック
 * http://www.cl.ecei.tohoku.ac.jp/nlp100/
 *
 * 第1章: 準備運動
 */
public class Chapter01Test {

    /**
     * 00. 文字列の逆順
     * 文字列"stressed"の文字を逆に（末尾から先頭に向かって）並べた文字列を得よ．
     */
    @Test
    public void q00() throws Exception {
        String str = "stressed";
        List<Character> list = new ArrayList<>();
        for (char c : str.toCharArray()) {
            list.add(c);
        }
        Collections.reverse(list);
        String result =
                list.stream()
                .map(c -> c.toString())
                .collect(Collectors.joining());

        assertThat(result, is("desserts"));
    }

    private List<Character> getCharacterList(String str) {
        List<Character> list = new ArrayList<>();
        for (char c : str.toCharArray()) {
            list.add(c);
        }
        return list;
    }

    /**
     * 01. 「パタトクカシーー」
     * 「パタトクカシーー」という文字列の1,3,5,7文字目を取り出して連結した文字列を得よ．
     */
    @Test
    public void q01() throws Exception {
        String str = "パタトクカシーー";
        String result = Stream.of(str.charAt(0), str.charAt(2), str.charAt(4), str.charAt(6))
                .map(c -> c.toString())
                .collect(Collectors.joining());

        assertThat(result, is("パトカー"));
    }

    /**
     * 02. 「パトカー」＋「タクシー」＝「パタトクカシーー」
     * 「パトカー」＋「タクシー」の文字を先頭から交互に連結して文字列「パタトクカシーー」を得よ．
     */
    @Test
    public void q02() throws Exception {
        String s1 = "パトカー";
        String s2 = "タクシー";

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < Math.min(s1.length(), s2.length()); i++) {
            sb.append(s1.charAt(i)).append(s2.charAt(i));
        }

        assertThat(sb.toString(), is("パタトクカシーー"));
    }

    /**
     * 03. 円周率
     * "Now I need a drink, alcoholic of course, after the heavy lectures involving quantum mechanics."
     * という文を単語に分解し，各単語の（アルファベットの）文字数を先頭から出現順に並べたリストを作成せよ．
     */
    @Test
    public void q03() throws Exception {
        String str = "Now I need a drink, alcoholic of course, after the heavy lectures involving quantum mechanics.";
        List<Integer> result = Stream.of(str.split("\\s+"))
                .map(s -> s.replaceAll("\\W", ""))
                .map(String::length)
                .collect(Collectors.toList());

        assertThat(result, contains(3, 1, 4, 1, 5, 9, 2, 6, 5, 3, 5, 8, 9, 7, 9));
    }

    /**
     * 04. 元素記号
     * "Hi He Lied Because Boron Could Not Oxidize Fluorine. New Nations Might Also Sign Peace Security Clause. Arthur King Can."
     * という文を単語に分解し，1, 5, 6, 7, 8, 9, 15, 16, 19番目の単語は先頭の1文字，
     * それ以外の単語は先頭に2文字を取り出し，
     * 取り出した文字列から単語の位置（先頭から何番目の単語か）への連想配列（辞書型もしくはマップ型）を作成せよ．
     */
    @Test
    public void q04() throws Exception {
        String str = "Hi He Lied Because Boron Could Not Oxidize Fluorine. New Nations Might Also Sign Peace Security Clause. Arthur King Can.";
        List<Integer> takeOne = Arrays.asList(0, 4, 5, 6, 7, 8, 14, 15, 18);
        String[] words = str.split("\\s+");

        Map<Integer, String> result = new LinkedHashMap<>();
        for (int i = 0; i < words.length; i++) {
            int take = takeOne.contains(i) ? 1 : 2;
            result.put(i + 1, words[i].substring(0, take));
        }

        Map<Integer, String> expect = new LinkedHashMap<>();
        expect.put( 1, "H");
        expect.put( 2, "He");
        expect.put( 3, "Li");
        expect.put( 4, "Be");
        expect.put( 5, "B");
        expect.put( 6, "C");
        expect.put( 7, "N");
        expect.put( 8, "O");
        expect.put( 9, "F");
        expect.put(10, "Ne");
        expect.put(11, "Na");
        expect.put(12, "Mi");
        expect.put(13, "Al");
        expect.put(14, "Si");
        expect.put(15, "P");
        expect.put(16, "S");
        expect.put(17, "Cl");
        expect.put(18, "Ar");
        expect.put(19, "K");
        expect.put(20, "Ca");

        assertThat(result, is(expect));
    }

    /**
     * 05. n-gram
     * 与えられたシーケンス（文字列やリストなど）からn-gramを作る関数を作成せよ．
     * この関数を用い，"I am an NLPer"という文から単語bi-gram，文字bi-gramを得よ．
     */
    @Test
    public void q05_文字bigram() throws Exception {
        String source = "I am an NLPer";

        List<String> charBiGram = charNGram(source, 2);
        assertThat(charBiGram, contains("I ", " a", "am", "m ", " a", "an", "n ", " N", "NL", "LP", "Pe", "er"));
    }

    private List<String> charNGram(String source, int n) {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < source.length() - n + 1; i++) {
            list.add(source.substring(i, i + n));
        }
        return list;
    }

    @Test
    public void q05_単語bigram() throws Exception {
        String source = "I am an NLPer";

        List<List<String>> wordBiGram =
                wordNGram(Arrays.asList(source.split("\\s+")), 2);
        List<List<String>> expect = Arrays.asList(
                Arrays.asList("I", "am"),
                Arrays.asList("am", "an"),
                Arrays.asList("an", "NLPer")
        );

        assertThat(wordBiGram, is(expect));
    }

    private List<List<String>> wordNGram(List<String> words, int n) {
        List<List<String>> list = new ArrayList<>();
        for (int i = 0; i < words.size() - n + 1; i++) {
            list.add(words.stream().skip(i).limit(n).collect(Collectors.toList()));
        }
        return list;
    }

    /**
     * 06. 集合
     * "paraparaparadise"と"paragraph"に含まれる文字bi-gramの集合を，
     * それぞれ, XとYとして求め，XとYの和集合，積集合，差集合を求めよ．
     * さらに，'se'というbi-gramがXおよびYに含まれるかどうかを調べよ．
     */
    @Test
    public void q06() throws Exception {
        Set<String> x = new LinkedHashSet<>(charNGram("paraparaparadise", 2));
        Set<String> y = new LinkedHashSet<>(charNGram("paragraph", 2));

        // 和集合
        Set<String> or = new LinkedHashSet<>(x);
        or.addAll(y);
        assertThat(or, contains("pa", "ar", "ra", "ap", "ad", "di", "is", "se", "ag", "gr", "ph"));

        // 積集合
        Set<String> and = new LinkedHashSet<>(x);
        and.retainAll(y);
        assertThat(and, contains("pa", "ar", "ra", "ap"));

        // 差集合
        Set<String> diff = new LinkedHashSet<>(x);
        diff.removeAll(y);
        assertThat(diff, contains("ad", "di", "is", "se"));

        // "se"が含まれるか
        assertTrue(x.contains("se"));
        assertFalse(y.contains("se"));
    }

    /**
     * 07. テンプレートによる文生成
     * 引数x, y, zを受け取り「x時のyはz」という文字列を返す関数を実装せよ．
     * さらに，x=12, y="気温", z=22.4として，実行結果を確認せよ．
     */
    @Test
    public void q07() throws Exception {
        assertThat(template(12, "気温", 22.4), is("12時の気温は22.4"));
    }

    private String template(int x, String y, double z) {
        return String.format("%d時の%sは%.1f", x, y, z);
    }

    /**
     * 08. 暗号文
     * 与えられた文字列の各文字を，以下の仕様で変換する関数cipherを実装せよ．
     *
     * 英小文字ならば(219 - 文字コード)の文字に置換
     * その他の文字はそのまま出力
     * この関数を用い，英語のメッセージを暗号化・復号化せよ．
     */
    @Test
    public void q08() throws Exception {
        String str = "I like Scala very well!";
        String encrypted = cipher(str);

        assertThat(encrypted, is("I orpv Sxzoz evib dvoo!"));
        assertThat(cipher(encrypted), is(str));
    }

    private String cipher(String str) {
        List<Character> chars = new ArrayList<>();
        for (char c : str.toCharArray()) {
            chars.add(c);
        }
        return chars.stream()
                .map(c -> 'a' <= c && c <= 'z' ? (char) (219 - c) : c)
                .map(c -> c.toString())
                .collect(Collectors.joining())
                ;
    }

    /**
     * 09. Typoglycemia
     * スペースで区切られた単語列に対して，各単語の先頭と末尾の文字は残し，それ以外の文字の順序をランダムに並び替えるプログラムを作成せよ．
     * ただし，長さが４以下の単語は並び替えないこととする．
     * 適当な英語の文
     * （例えば"I couldn't believe that I could actually understand what I was reading : the phenomenal power of the human mind ."）
     * を与え，その実行結果を確認せよ．
     */
    @Test
    public void q09() throws Exception {
        String str = "Scala is object oriented , functional , and scalable programming language .";
        Random random = new Random(999);

        String result =
                Stream.of(str.split("\\s+"))
                .map(s -> 5 <= s.length() ? shuffleMiddle(s, random) : s)
                .collect(Collectors.joining(" "))
                ;
        assertThat(result, is("Sclaa is ocbjet oeetrnid , fciotnnaul , and scaballe pngamimrrog lnauagge ."));
    }

    private String shuffleMiddle(String s, Random random) {
        String first = s.substring(0, 1);
        String last  = s.substring(s.length() - 1, s.length());
        List<Character> middleChars = getCharacterList(s.substring(1, s.length() - 1));
        Collections.shuffle(middleChars, random);
        String middle = middleChars.stream().map(c -> c.toString()).collect(Collectors.joining());
        return first + middle + last;
    }
}