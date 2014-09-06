package frust_o_mat;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class FrustOMatSimpleImpl implements FrustOMat {

	private List<String> topics = new ArrayList<String> ();
	private List<String> votes = new LinkedList<String>();
	
	@Override
	public List<String> getTopics() {
		return topics;
	}

	@Override
	public void addTopic(String topic) {
		topics.add(topic);
	}

	@Override
	public void voteForTopic(String topic) throws NonExistingTopicException {
		if (topics.contains(topic))
		{
			votes.add (topic);
		}
		else
		{
			throw new NonExistingTopicException(topic);
		}
	}

	@Override
	public int getCountFor(String topic) throws NonExistingTopicException {
		if (topics.contains(topic))
		{
			return count(topic, votes);
		}
		else
		{
			throw new NonExistingTopicException(topic);
		}
	}

	private int count(String topic, List<String> votes2) {
		int result = 0;
		for (String eachVote : votes) {
			if (eachVote.equals(topic))
			{
				result++;
			}
		}
		return result;
	}
}
