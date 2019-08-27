package jp.project3.testsumlist;

import static jp.project3.testsumlist.Define.*;

import java.util.Arrays;
import java.util.StringJoiner;

/**
 * 生徒個人の成績情報を保持するクラス
 *
 */
class Student {
	private String name;//生徒氏名
	public int[] scoreArray;//得点配列 教科の順番は呼び出し側に依存
	private int sumRanking;//合計点順位
	private boolean reTester;//再試験者フラグ trueは再試験
	/**
	 * 生徒情報を生成
	 * @param name 生徒名
	 */
	public Student(String[] studentInfos) {
		this.name = studentInfos[ZERO];
		scoreArray = new int[studentInfos.length - ONE];
		reTester = false;
		int index1 = ZERO;
		int index2 = ONE;
		while(index2 < studentInfos.length) {
			scoreArray[index1] = Integer.parseInt(studentInfos[index2]);
			index1++;
			index2++;
		}
	}

	/**
	 * 教科の得点を取得する
	 * @param index 教科のインデックス
	 * @return 教科の得点
	 */
	public Integer getScore(int index) {
		return scoreArray[index];
	}
	/**
	 * 教科の合計点を取得
	 * @return 合計点
	 */
	public int getSumScore() {
		return Arrays.stream(scoreArray).reduce(ZERO,Integer::sum);
	}
	/**
	 * 合計点順位を取得する
	 * @return 合計点順位
	 */
	public int getSumRanking() {
		return sumRanking;
	}
	/**
	 * 合計点順位を設定する
	 * @param sumRanking 合計点順位
	 */
	public void setSumRanking(int sumRanking) {
		this.sumRanking = sumRanking;
	}
	/**
	 * 生徒の氏名を取得する
	 * @return 生徒の氏名
	 */
	public String getName() {
		return name;
	}
	/**
	 * 再試験者フラグを取得する
	 * @return 再試験の場合はtrue
	 */
	public boolean getReTestFlag(int failscore,int absence) {
		for(int index = ZERO;index < scoreArray.length;index++) {
			int wkScore = scoreArray[index];
			if(wkScore <= failscore || wkScore == absence) {
				return true;
			}
		}
		return false;
	}
	public int getSubjectSize() {
		return scoreArray.length;
	}
	@Override
	public String toString() {//テスト用
		StringJoiner sj = new StringJoiner(",");
		sj.add(name);
		for(int score : scoreArray) {
			sj.add(String.valueOf(score));
		}
		sj.add(String.valueOf(this.getSumScore()));
		return sj.toString();
	}


}
