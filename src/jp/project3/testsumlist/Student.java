package jp.project3.testsumlist;

import static jp.project3.testsumlist.Define.*;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

/**
 * 生徒個人の成績情報を保持するクラス
 *
 */
class Student {
	private String name;//生徒氏名
	private List<Integer> scoreList;//得点リスト 教科の順番は呼び出し側に依存
	private int sumRanking;//合計点順位
	private boolean reTester;//再試験者フラグ trueは再試験
	/**
	 * 生徒情報を生成
	 * @param name 生徒名
	 */
	public Student(String name) {
		this.name = name;
		scoreList = new ArrayList<>();
		reTester = false;
	}
	public Student(String[] studentInfos) {
		this.name = studentInfos[ZERO];
		scoreList = new ArrayList<>();
		reTester = false;
		for(int index = ONE; index < studentInfos.length;index++) {
			scoreList.add(Integer.parseInt(studentInfos[index]));
		}
	}
	/**
	 * 教科の点数を追加する
	 * @param score 追加する教科の点数
	 */
	public void addScore(int score) {
		scoreList.add(score);
	}
	/**
	 * 教科の得点を取得する
	 * @param index 教科のインデックス
	 * @return 教科の得点
	 */
	public Integer getScore(int index) {
		return scoreList.get(index);
	}
	/**
	 * 教科の合計点を取得
	 * @return 合計点
	 */
	public int getSumScore() {
		return scoreList.stream().reduce(ZERO,Integer::sum);
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
		boolean flag = false;
		for(int index = ZERO;index < scoreList.size();index++) {
			int wkScore = scoreList.get(index);
			if(wkScore <= failscore || wkScore == absence) {
				return false;
			}
		}
		return true;
	}
	public int getSubjectSize() {
		return scoreList.size();
	}
	@Override
	public String toString() {//テスト用
		StringJoiner sj = new StringJoiner(",");
		sj.add(name);
		scoreList.forEach(st -> sj.add(String.valueOf(st)));
		sj.add(String.valueOf(this.getSumScore()));
		return sj.toString();
	}


}
