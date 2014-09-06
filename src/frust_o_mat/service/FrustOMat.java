package frust_o_mat.service;

import java.util.List;

public interface FrustOMat {

	List<String> getTopics();

	void addTopic(String topic);

	void voteForTopic(String topic)
			throws NonExistingTopicException;

	int getCountFor(String topic)
			throws NonExistingTopicException;

}