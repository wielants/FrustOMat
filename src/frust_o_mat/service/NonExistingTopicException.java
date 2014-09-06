package frust_o_mat.service;

public class NonExistingTopicException extends RuntimeException {

	public NonExistingTopicException(String topic) {
		super("topic '" + topic + "' does not exist!");
	}

}
