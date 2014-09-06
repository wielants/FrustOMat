package frust_o_mat.service.test;

import frust_o_mat.service.FrustOMat;
import frust_o_mat.service.FrustOMatSimpleImpl;

public class FrustOMatSimpleImplTest extends AbstractFrustOMatTest {

	@Override
	protected FrustOMat createInstance() {
		return new FrustOMatSimpleImpl();
	}
}
