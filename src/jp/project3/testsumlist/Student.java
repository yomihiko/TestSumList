package jp.project3.testsumlist;

import static jp.project3.testsumlist.Define.*;

import java.util.Arrays;

/**
 * 生徒個人の成績情報を保持するクラス
 *
 */
class Student {
	private String name;//生徒氏名
	public int[] scoreArray;//得点配列 教科の順番は呼び出し側に依存
	private int sumRanking;//合計点順位
	/**
	 * 生徒情報を生成
	 * @param name 生徒名
	 */
	public Student(String[] studentInfos) {
		this.name = studentInfos[ZERO];
		scoreArray = new int[studentInfos.length - ONE];
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
		return Arrays.stream(scoreArray).anyMatch(st -> st <= failscore || st == absence);
	}
	/**
	 * 教科数を返す
	 * @return 教科数
	 */
	public int getSubjectSize() {
		return scoreArray.length;
	}
	/**
	 * 全教科の得点を返す
	 * @return 各教科の得点を格納したint型配列
	 */
	public int[] getSubjectScores(){
		return scoreArray;
	}
}
