package com.tundem.alternativefindr.entity.sort;

import java.util.Comparator;

import com.tundem.alternativefindr.entity.Application;

public class SortApplication implements Comparator<Application> {
	@Override
	public int compare(Application object1, Application object2) {
		double ojbect1_votes = object1.getVotes();
		double ojbect2_votes = object2.getVotes();

		if (ojbect1_votes < ojbect2_votes) {
			return 1;
		} else if (ojbect1_votes == ojbect2_votes) {
			return 0;
		} else {
			return -1;
		}
	}
}
