package org.nomadblacky.nlp100.kotlin

import org.hamcrest.Matchers.*
import org.junit.Assert.*
import org.junit.Test
import java.util.*

/**
 * 自然言語処理100本ノック
 * http://www.cl.ecei.tohoku.ac.jp/nlp100/
 *
 * 第1章: 準備運動
 */
class Chapter01Test {

    /**
     * 00. 文字列の逆順
     * 文字列"stressed"の文字を逆に（末尾から先頭に向かって）並べた文字列を得よ．
     */
    @Test
    @Throws(Exception::class)
    fun q00() {
        assertThat("stressed".reversed(), `is`("desserts"))
    }

    /**
     * 01. 「パタトクカシーー」
     * 「パタトクカシーー」という文字列の1,3,5,7文字目を取り出して連結した文字列を得よ．
     */
    @Test
    @Throws(Exception::class)
    fun q01() {
        val str = "パタトクカシーー"
        val result =
                listOf(str.get(0), str.get(2), str.get(4), str.get(6))
                .joinToString(separator = "")
        assertThat(result, `is`("パトカー"))
    }

    /**
     * 02. 「パトカー」＋「タクシー」＝「パタトクカシーー」
     * 「パトカー」＋「タクシー」の文字を先頭から交互に連結して文字列「パタトクカシーー」を得よ．
     */
    @Test
    @Throws(Exception::class)
    fun q02() {
        val s1 = "パトカー"
        val s2 = "タクシー"
        val result =
                s1.zip(s2) { a, b ->
                    a.toString() + b.toString()
                }.joinToString(separator = "")

        assertThat(result, `is`("パタトクカシーー"))
    }


    /**
     * 03. 円周率
     * "Now I need a drink, alcoholic of course, after the heavy lectures involving quantum mechanics."
     * という文を単語に分解し，各単語の（アルファベットの）文字数を先頭から出現順に並べたリストを作成せよ．
     */
    @Test
    @Throws(Exception::class)
    fun q03() {
        val str = "Now I need a drink, alcoholic of course, after the heavy lectures involving quantum mechanics."
        val result = str.split("""\s+""".toRegex())
                .map { it.replace("""\W+""".toRegex(), "") }
                .map(String::length)

        assertThat(result, contains(3, 1, 4, 1, 5, 9, 2, 6, 5, 3, 5, 8, 9, 7, 9))
    }

    /**
     * 04. 元素記号
     * "Hi He Lied Because Boron Could Not Oxidize Fluorine. New Nations Might Also Sign Peace Security Clause. Arthur King Can."
     * という文を単語に分解し，1, 5, 6, 7, 8, 9, 15, 16, 19番目の単語は先頭の1文字，
     * それ以外の単語は先頭に2文字を取り出し，
     * 取り出した文字列から単語の位置（先頭から何番目の単語か）への連想配列（辞書型もしくはマップ型）を作成せよ．
     */
    @Test
    @Throws(Exception::class)
    fun q04() {
        val text = "Hi He Lied Because Boron Could Not Oxidize Fluorine. New Nations Might Also Sign Peace Security Clause. Arthur King Can."
        val extractOne = listOf(0, 4, 5, 6, 7, 8, 14, 17, 18)
        val result = text.split("""\s+""".toRegex())
                .mapIndexed { i, s ->
                    if (extractOne.contains(i)) i + 1 to s.take(1) else i + 1 to s.take(2)
                }.toMap()

        val expect: Map<Int, String> = linkedMapOf(
                 1 to "H" ,
                 2 to "He",
                 3 to "Li",
                 4 to "Be",
                 5 to "B" ,
                 6 to "C" ,
                 7 to "N" ,
                 8 to "O" ,
                 9 to "F" ,
                10 to "Ne",
                11 to "Na",
                12 to "Mi",
                13 to "Al",
                14 to "Si",
                15 to "P" ,
                16 to "S" ,
                17 to "Cl",
                18 to "Ar",
                19 to "K" ,
                20 to "Ca"
        )

        assertThat(result, `is`(expect))
    }

    /**
     * 05. n-gram
     * 与えられたシーケンス（文字列やリストなど）からn-gramを作る関数を作成せよ．
     * この関数を用い，"I am an NLPer"という文から単語bi-gram，文字bi-gramを得よ．
     */
    @Test
    @Throws(Exception::class)
    fun q05_文字bigram() {
        val source = "I am an NLPer"

        val charBiGram = charNGram(source, 2)
        assertThat(charBiGram, contains("I ", " a", "am", "m ", " a", "an", "n ", " N", "NL", "LP", "Pe", "er"))
    }

    private fun charNGram(source: String, n: Int): List<String> {
        return (0..source.length - n).map { source.substring(it, it + n) }
    }

    @Test
    @Throws(Exception::class)
    fun q05_単語bigram() {
        val source = "I am an NLPer"

        val wordBiGram = wordNGram(source.split("""\s+""".toRegex()), 2)

        assertThat(wordBiGram, contains(
                Arrays.asList("I", "am"),
                Arrays.asList("am", "an"),
                Arrays.asList("an", "NLPer")
        ))
    }

    private fun wordNGram(words: List<String>, n: Int): List<List<String>> {
        return (0..words.size - n).map { words.slice(it..it + n -1) }
    }

    /**
     * 06. 集合
     * "paraparaparadise"と"paragraph"に含まれる文字bi-gramの集合を，
     * それぞれ, XとYとして求め，XとYの和集合，積集合，差集合を求めよ．
     * さらに，'se'というbi-gramがXおよびYに含まれるかどうかを調べよ．
     */
    @Test
    @Throws(Exception::class)
    fun q06() {
        val x: Set<String> = charNGram("paraparaparadise", 2).toSet()
        val y: Set<String> = charNGram("paragraph", 2).toSet()

        // 和集合
        assertThat(x + y, contains("pa", "ar", "ra", "ap", "ad", "di", "is", "se", "ag", "gr", "ph"))
        // 積集合
        assertThat(x intersect y, contains("pa", "ar", "ra", "ap"))
        // 差集合
        assertThat(x - y, contains("ad", "di", "is", "se"))
        // "se"が含まれるか
        assertTrue(x.contains("se"))
        assertFalse(y.contains("se"))
    }

    /**
     * 07. テンプレートによる文生成
     * 引数x, y, zを受け取り「x時のyはz」という文字列を返す関数を実装せよ．
     * さらに，x=12, y="気温", z=22.4として，実行結果を確認せよ．
     */
    @Test
    @Throws(Exception::class)
    fun q07() {
        assertThat(template(12, "気温", 22.4), `is`("12時の気温は22.4"))
    }

    private fun template(x: Int, y: String, z: Double): String {
        return "%d時の%sは%.1f".format(x, y, z)
    }

    private fun template2(x: Int, y: String, z: Double): String {
        return "${x}時の${y}は$z"
    }

    /**
     * 08. 暗号文
     * 与えられた文字列の各文字を，以下の仕様で変換する関数cipherを実装せよ．

     * 英小文字ならば(219 - 文字コード)の文字に置換
     * その他の文字はそのまま出力
     * この関数を用い，英語のメッセージを暗号化・復号化せよ．
     */
    @Test
    @Throws(Exception::class)
    fun q08() {
        fun cipher(text: String): String {
            return text.map { if (it.isLowerCase()) (219 - it.toInt()).toChar() else it }.joinToString(separator = "")
        }

        val str = "I like Scala very well!"
        val encrypted = cipher(str)

        assertThat(encrypted, `is`("I orpv Sxzoz evib dvoo!"))
        assertThat(cipher(encrypted), `is`(str))
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
    @Throws(Exception::class)
    fun q09() {
        fun <T> shuffle(list: List<T>, random: Random): List<T> {
            val mList = list.toMutableList()
            Collections.shuffle(mList, random)
            return mList
        }
        fun typoglycemia(text: String, random: Random): String {
            val words = text.split(Regex("\\s"))
            return words.map {
                if (5 <= it.length) {
                    val middle = it.drop(1).dropLast(1).let { s -> shuffle(s.toList(), random) }.joinToString(separator = "")
                    "${it.first()}$middle${it.last()}"
                } else {
                    it
                }
            }.joinToString(separator = " ")
        }

        val str = "Scala is object oriented , functional , and scalable programming language ."
        val random = Random(999)

        assertThat(
                typoglycemia(str, random),
                `is`("Sclaa is ocbjet oeetrnid , fciotnnaul , and scaballe pngamimrrog lnauagge .")
        )
    }
}