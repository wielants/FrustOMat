package frust_o_mat.service;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class FrustOMatPersistentImpl implements FrustOMat {

	@Override
	public synchronized List<String> getTopics() {
		try {
			return Files.readAllLines(Paths.get("topics.txt"), Charset.forName("UTF-8"));
		} catch (IOException e) {
			throw new RuntimeException (e);
		}
	}

	@Override
	public synchronized void addTopic(String topic) {
		try(PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("topics.txt", true)))) {
		    out.println(topic);
		}catch (IOException e) {
			throw new RuntimeException (e);
		}
	}

	@Override
	public synchronized void voteForTopic(String topic) throws NonExistingTopicException {
		List<String> topics = getTopics ();
		if (topics.contains(topic))
		{
			try(PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("votes.txt", true)))) {
			    out.println(topic);
			}catch (IOException e) {
				throw new RuntimeException (e);
			}
		}
		else
		{
			throw new NonExistingTopicException(topic);
		}
	}

	@Override
	public synchronized int getCountFor(String topic) throws NonExistingTopicException {
		List<String> topics = getTopics ();
		if (topics.contains(topic))
		{
			List<String> votes;
			try {
				votes = Files.readAllLines(Paths.get("votes.txt"), Charset.forName("UTF-8"));
			} catch (IOException e) {
				throw new RuntimeException (e);
			}
			return count(topic, votes);
		}
		else
		{
			throw new NonExistingTopicException(topic);
		}
	}

	private synchronized int count(String topic, List<String> votes) {
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
