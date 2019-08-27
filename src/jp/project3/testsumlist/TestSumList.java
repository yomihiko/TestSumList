package jp.project3.testsumlist;

import static jp.project3.testsumlist.Define.*;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TestSumList {
	private static final int FAILSCORE = 25;//赤点
	private static final int ABSENCE = -1;//欠席を表す数値
	private static final String FILEPATH = "bin/scorelist.txt";//成績ファイルのパス
	private static final String CHARSET = "UTF-8";//成績ファイルの文字コード
	private static final String HALFCOMMA = ",";//成績ファイルの区切り文字
	private static final String HIGHSCOREMARK = "*";//教科最高点取得者を表す印
	private static final String HALFSPACE = " ";//半角空白
	private static final String FULLSPACE = "　";//全角空白
	private static final String DIGIT1 = "%";//桁揃え用
	private static final String DIGIT2 = "d";//桁揃え用
	private static final String DIGIT3 = "%1$-";//文字列揃え用
	private static final String DIGIT4 = "s";//文字列揃え用
	private static final String[] SUBJECTARRAY = {"国語","数学","英語"};//教科
	private static final String LABEL1 = "【試験成績順位】";
	private static final String LABEL2 = "【再試験者】";
	/**
	 * INFOREGIX 不正データを排除する正規表現 得点の範囲も正規表現で表す
	 */
	private static final String INFOREGIX = "^[^"+HALFCOMMA+"]+(" + HALFCOMMA +
												"("+ABSENCE+"|[0-9]|[1-9][0-9]|100)){"+SUBJECTARRAY.length+"}$";



	public static void main(String[] args) {
		try {
			List<String> scoreStList = Files.readAllLines(Path.of(FILEPATH), Charset.forName(CHARSET));//ファイルの読み込み
			List<Student> scoreStArList = scoreStList.stream()
					.filter(
							st -> st.matches(INFOREGIX)) //不正データを除去
					.map(
						st -> st.split(HALFCOMMA)//区切り文字で区切る
					).map(Student::new).collect(Collectors.toList());
			ArrayList<Student> failStudentList = new ArrayList<>();//再試験者のデータのリスト
			ArrayList<Student> trueStudentList = new ArrayList<>();//合格者のリスト
			int[] subjectMaxScores = new int[SUBJECTARRAY.length];//教科の最高点 インデックスはSUBJECTARRAYと対応
			int[] subjectMaxLen = new int[SUBJECTARRAY.length];//教科の最高点の桁数 同上
			for(Student st : scoreStArList) {
				if(st.getReTestFlag(FAILSCORE, ABSENCE)) {//赤点 or 欠席の時
					failStudentList.add(st);
				}else { //合格の時
					trueStudentList.add(st);
					for(int index = ZERO;index < SUBJECTARRAY.length;index++) {//教科最高点と最大桁数を調べる
						if(subjectMaxScores[index] < st.getScore(index))
							subjectMaxScores[index] = st.getScore(index);//最高点を上書き
						if(subjectMaxLen[index] < String.valueOf(st.getScore(index)).length())
							subjectMaxLen[index] = String.valueOf(st.getScore(index)).length();//最大桁数を上書き
					}
				}
			}
			if(trueStudentList.size() > ZERO) { //合格者がいるとき
				trueStudentList = trueStudentList.stream() //合格者のリスト
						.sorted((x,y) -> Integer.compare(y.getSumScore(), x.getSumScore())
								)//合計点で降順にソート
						.collect(Collectors.toCollection(ArrayList::new));

				int rankingMaxLen = (int)trueStudentList.stream().mapToInt(st -> st.getSumScore()).count();
				rankingMaxLen = String.valueOf(rankingMaxLen).length();
				//順位の最大桁数を求める
				int nameMaxLen = trueStudentList.stream().mapToInt(st -> st.getName().length()).max().orElse(ZERO);
				//氏名の最大文字列長を求める
				System.out.println(LABEL1);//ラベルを表示
				int tmpRank = ONE;
				Student tmpSt = trueStudentList.get(ZERO);
				for(Student st : trueStudentList) {
					if(tmpSt.getSumScore() > st.getSumScore()) tmpRank++;//前の人より点が低い場合は順位を加算
					System.out.printf(DIGIT1 + rankingMaxLen +DIGIT2, tmpRank);//ランキングを表示
					System.out.print(FULLSPACE);
					int tmpLen = (nameMaxLen - st.getName().length()) * 2 + st.getName().length();
					//この生徒の名前の文字数半角スペースをいくつ足せば最大文字列長と等しくなるか
					System.out.printf(DIGIT3 + tmpLen +DIGIT4, st.getName());
					for(int index = ZERO;index < SUBJECTARRAY.length;index++) {
						String wkMark = HALFSPACE;//付与するマークを一時的に保存する変数
						System.out.print(FULLSPACE);
						if(st.getScore(index) == subjectMaxScores[index]) wkMark = HIGHSCOREMARK;
						//教科最高点の場合はマーク
						System.out.print(wkMark);
						System.out.printf(DIGIT1 + subjectMaxLen[index] +DIGIT2, st.getScore(index));
						//教科の点数を表示
					}
					tmpSt = st;
					System.out.println();
				}
			}
			System.out.println(LABEL2);
			if(failStudentList.size() == ZERO) {
				System.out.println("該当者なし");
			}else {
				failStudentList.forEach(st -> System.out.println(st.getName()));
			}





		}catch (IOException e) {
			e.printStackTrace();
			System.exit(ABNORMAL);
		}
	}
}
