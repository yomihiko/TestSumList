package jp.project3.testsumlist;

import static jp.project3.testsumlist.Define.*;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

public class TestSumList {
	private static final int FAILSCORE = 25;//赤点
	private static final int MINSCORE = 0;//テストの最低点
	private static final int MAXSCORE = 100;//テストの最高点
	private static final int ABSENCE = -1;//欠席を表す数値
	private static final String FILEPATH = "bin/scorelist.txt";//成績ファイルのパス
	private static final String CHARSET = "MS932";
	private static final String HALFCOMMA = ",";//成績ファイルの区切り文字
	private static final String HIGHSCOREMARK = "*";//教科最高点取得者を表す印
	private static final String HALFSPACE = " ";//半角空白
	private static final String FULLSPACE = "　";//全角空白
	private static final String DIGIT1 = "%";//桁揃え用
	private static final String DIGIT2 = "d";//桁揃え用
	private static final String DIGIT3 = "s";//文字列揃え用
	private static final String INFOREGIX = "^[^"+HALFCOMMA+"]+" + HALFCOMMA + "(-?[1-9][0-9]*|0)+$";
	private static final String[] SUBJECTARRAY = {"国語","数学","英語"};//教科



	public static void main(String[] args) {
		try {
			System.out.println(INFOREGIX);
			List<String> scoreStList = Files.readAllLines(Path.of(FILEPATH), Charset.forName(CHARSET));//ファイルの読み込み
			scoreStList.stream().forEach(st -> System.out.println(st.matches(INFOREGIX)));
			List<Student> scoreStArList = scoreStList.stream()
					.filter(st -> st.matches(INFOREGIX))
					.map(
						st -> st.split(HALFCOMMA)//区切り文字で区切る
					)//.filter(
						//st -> checkScoreInfo(st) //不正データチェック
					.map(Student::new).collect(Collectors.toList());
			scoreStArList = scoreStArList.stream()
				.sorted((x,y) -> Integer.compare(y.getSumScore(), x.getSumScore()))//合計点で降順にソート
				.collect(Collectors.toList());
			scoreStArList.forEach(System.out::println);
			List<Student> trueStudentList = scoreStArList.stream() //合格者のリスト
					.filter(st -> st.getReTestFlag(FAILSCORE, ABSENCE) == false)
					.collect(Collectors.toList());
			List<Student> failStudentList = scoreStArList.stream() //再試験者のリスト
					.filter(st -> st.getReTestFlag(FAILSCORE, ABSENCE))
					.collect(Collectors.toList());




		}catch (IOException e) {
			System.exit(ABNORMAL);
		}
	}
	/**
	 * 分割した文字列配列が不正データでないかをチェックするメソッド
	 * @param scoresInfo 分割した文字列配列
	 * @return 正常データの場合はtrue 不正データの場合はfalse
	 */
//	public static boolean checkScoreInfo(String[] scoresInfo) {
//		if(scoresInfo.length != SUBJECTARRAY.length + ONE) { //得点の教科数が異なる場合は不正データ
//			return false;
//		}
//		try {
//			int count = (int)Arrays.stream(scoresInfo)
//				.filter(st -> st.matches(NUMBERREGIX))//先頭に0がないか、全角でないか、氏名は除去
//				.map(st -> Integer.parseInt(st))//数値型に変える
//				.filter(score -> score >= MINSCORE && score <= MAXSCORE || score == ABSENCE)//得点の範囲内か
//				.count();
//			return count == SUBJECTARRAY.length;//正しいデータが教科数と等しいならtrue
//		}catch(NumberFormatException e) {//int型範囲外の場合はfalse
//			return false;
//		}
//	}

}
