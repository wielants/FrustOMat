package frust_o_mat.service;

public class AlreadyExistingTopicException extends RuntimeException {
	public AlreadyExistingTopicException(String topic) {
		super("topic '" + topic + "' does already exist!");
	}
}
