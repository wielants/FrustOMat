package frust_o_mat;

import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.hasItem;

import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public abstract class AbstractFrustOMatTest {
	
	private FrustOMat fom;
	
	@Before
	public void init () throws InstantiationException, IllegalAccessException
	{
		fom = createInstance ();
	}
	
	protected abstract FrustOMat createInstance();
	
	@Test
	public void testGetEmptyTopics ()
	{
		List<String> topics = fom.getTopics ();
		Assert.assertEquals(0, topics.size ());
	}
	
	@Test
	public void testAddTopics ()
	{
		fom.addTopic (getTopic1());
		fom.addTopic (getTopic2());
		
		List<String> topics = fom.getTopics ();
		Assert.assertEquals(2, topics.size ());
		Assert.assertThat(topics, hasItem(getTopic1()));
		Assert.assertThat(topics, hasItem(getTopic2()));
	}
	
	@Test
	public void testAddAlreadyExistingTopic ()
	{
		fom.addTopic (getTopic1());
		
		try
		{
			fom.addTopic(getTopic1());
		}
		catch (AlreadyExistingTopicException e)
		{
			List<String> topics = fom.getTopics();
			Assert.assertEquals(1, topics.size());
			Assert.assertThat(topics, hasItem(getTopic1()));
		}
	}
	
	@Test
	public void testVoteForNonExistingTopic ()
	{
		try
		{
			fom.voteForTopic (getTopic1());
			Assert.fail();
		}
		catch (NonExistingTopicException e)
		{
			// ok
		}
	}

	@Test
	public void testVoteForExistingTopic ()
	{
		fom.addTopic (getTopic1());
		
		fom.voteForTopic (getTopic1());
	}

	@Test
	public void testCountOfNonExistingTopic ()
	{
		try
		{
			int count = fom.getCountFor (getTopic1());
		}
		catch (NonExistingTopicException e)
		{
			Assert.assertThat(e.getMessage(), allOf(containsString (getTopic1()), containsString ("does not exist")));
		}
	}

	@Test
	public void testCountOfExistingTopic ()
	{
		fom.addTopic (getTopic1());
		
		try
		{
			int count = fom.getCountFor (getTopic1());
		}
		catch (NonExistingTopicException e)
		{
			Assert.assertThat(e.getMessage(), allOf(containsString (getTopic1()), containsString ("does not exist")));
		}
	}

	@Test
	public void testCountOfExistingAndVotedTopic ()
	{
		fom.addTopic (getTopic1());
		fom.voteForTopic(getTopic1());
		
		try
		{
			int count = fom.getCountFor (getTopic1());
		}
		catch (NonExistingTopicException e)
		{
			Assert.assertThat(e.getMessage(), allOf(containsString (getTopic1()), containsString ("does not exist")));
		}
	}
	
	@Test
	public void testCountOfVotedTopic ()
	{
		fom.addTopic (getTopic1());
		fom.addTopic (getTopic2());
		fom.voteForTopic(getTopic2());
		fom.voteForTopic(getTopic1());
		fom.voteForTopic(getTopic2());
		fom.voteForTopic(getTopic2());
		
		int count1 = fom.getCountFor (getTopic1());
		Assert.assertEquals(1, count1);
		int count2 = fom.getCountFor (getTopic2());
		Assert.assertEquals(3, count2);
	}

	private String getTopic1() {
		return "Keiner ist zustaendig!";
	}

	private String getTopic2() {
		return "Eclipse ist langsam!";
	}
	
}
