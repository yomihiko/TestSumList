package jp.project3.testsumlist;

import static jp.project3.testsumlist.Define.*;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 *
 * 生徒の試験成績と再試験者を表示するクラス
 *
 */
public class TestSumList {
	private static final int FAILSCORE = 25;					//赤点
	private static final int ABSENCE = -1;						//欠席を表す数値
	private static final int SUBJECTNUM = 3;					//教科数
	private static final String FILEPATH = "bin/tests.txt";		//成績ファイルのパス
	private static final String CHARSET = "MS932";				//成績ファイルの文字コード
	private static final String HALFCOMMA = ",";				//成績ファイルの区切り文字
	private static final String HIGHSCOREMARK = "*";			//教科最高点取得者を表す印
	private static final String HALFSPACE = " ";				//半角空白
	private static final String FULLSPACE = "　";				//全角空白
	private static final String DIGIT1 = "%";					//桁揃え用
	private static final String DIGIT2 = "d";					//桁揃え用
	private static final String DIGIT3 = "%1$-";				//文字列揃え用
	private static final String DIGIT4 = "s";					//文字列揃え用
	private static final String LABEL1 = "【試験成績順位】";	//表示用
	private static final String LABEL2 = "【再試験者】";		//表示用
	private static final String LABEL3 = "該当者なし";			//表示用
	/**
	 * INFOREGIX 不正データを排除する正規表現 得点の範囲も正規表現で表す
	 */
	private static final String INFOREGIX = "^[^"+HALFCOMMA+"]+(" + HALFCOMMA +
												"("+ABSENCE+"|[0-9]|[1-9][0-9]|100)){"+SUBJECTNUM+"}$";
	/**
	 * 成績ファイルを読み込み成績順位と再試験者を表示するメソッド
	 */
	public static void main(String[] args) {
		List<Student> scoreStArList = List.of();
		try(Stream<String> tmpSt = Files.lines(Path.of(FILEPATH), Charset.forName(CHARSET))) {
			scoreStArList = tmpSt
					.filter(
							st -> st.matches(INFOREGIX)) //不正データを除去
					.map(
						st -> st.split(HALFCOMMA)//区切り文字で区切る
					).map(Student::new).collect(Collectors.toList());
		}catch (IOException e) {
			// TODO: handle exception
			System.out.println(E001);
			System.out.println(I001);
			System.exit(ABNORMAL);//ファイルが読み込めない場合は異常終了
		}
		Student[] failStAr;//再試験者のデータの配列
		Student[] trueStAr;//合格者の配列
		int[] subjectMaxScores = new int[SUBJECTNUM];//教科の最高点 インデックスはSUBJECTARRAYと対応
		int[] subjectMaxLen = new int[SUBJECTNUM];//教科の最高点の桁数 同上
		failStAr = scoreStArList.stream().filter(st -> st.getReTestFlag(FAILSCORE, ABSENCE)).toArray(Student[]::new);
		//再試験者を抽出
		trueStAr = scoreStArList.stream().filter(st -> ! st.getReTestFlag(FAILSCORE, ABSENCE)).toArray(Student[]::new);
		//合格者を抽出
		subjectMaxScores = Stream.iterate(ZERO,i -> i < SUBJECTNUM ,i -> ++i)//教科ごとの最高得点の桁数を求める
				.map(i -> subjectScoresArray(i, trueStAr))
				.mapToInt(sal -> Arrays.stream(sal).max().orElse(ZERO))//得られた配列の中から最大値を求める
				.toArray();
		subjectMaxLen = Arrays.stream(subjectMaxScores)//教科ごとの最高得点の桁数を求める
				.map(s -> String.valueOf(s).length())
				.toArray();
		if(trueStAr.length > ZERO) { //合格者がいるとき
			Arrays.sort(trueStAr,Comparator.comparingInt(Student::getSumScore).reversed().thenComparing(Student::getName));
			int nameMaxLen = Arrays.stream(trueStAr).mapToInt(st -> st.getName().length()).max().orElse(ZERO);
			//氏名の最大文字列長を求める
			int rank = ONE;//順位をカウント
			trueStAr[ZERO].setSumRanking(rank);//先頭は一位
			for(int index = ONE;index < trueStAr.length;index++) {//順位付けをする
				rank = trueStAr[index].getSumScore() == trueStAr[index - ONE].getSumScore()
						? rank : index + ONE;//前の人と点が同じときは同順位、違うときは適切な順位を
				trueStAr[index].setSumRanking(rank);
			}
			int rankingMaxLen =  String.valueOf(rank).length();
			System.out.println(LABEL1);//ラベルを表示
			for(Student st : trueStAr) {
				int tmpLen = (nameMaxLen - st.getName().length()) * TWO + st.getName().length();
				//この生徒の名前の文字数半角スペースをいくつ足せば最大文字列長と等しくなるか
				String tmpStr = String.join(FULLSPACE,
						String.format(DIGIT1 + rankingMaxLen +DIGIT2, st.getSumRanking()),
						String.format(DIGIT3 + tmpLen +DIGIT4, st.getName()));
				for(int index = ZERO;index < SUBJECTNUM;index++) {
					String wkMark = st.getScore(index) == subjectMaxScores[index] ? HIGHSCOREMARK : HALFSPACE;
					//付与するマークを一時的に保存する変数
					tmpStr = String.join(FULLSPACE,
							tmpStr,
							String.format(wkMark + DIGIT1 + subjectMaxLen[index] +DIGIT2, st.getScore(index)));
				}
				System.out.println(tmpStr);
			}
		}
		System.out.println(LABEL2);//再試験者ラベル
		if(failStAr.length == ZERO) {//再試験者がいないとき
			System.out.println(LABEL3);
		}else {
			Arrays.stream(failStAr).forEach(st -> System.out.println(st.getName()));
		}
		System.out.println();
		System.out.println(I001);
	}
	/**
	 * 教科ごとの得点の配列を取得するメソッド
	 * @param index 教科インデックス
	 * @param students //Studentインスタンス
	 * @return 教科ごとの得点のint型配列
	 */
	public static int[] subjectScoresArray(int index,Student...students) {
		return Arrays.stream(students).mapToInt(st -> st.getScore(index)).toArray();
	}
}