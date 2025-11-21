package simplepayroll_public.repo;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import local.payrollapp.simplepayroll.components.MockDatabase;

@ExtendWith(MockitoExtension.class)
public class JobsiteRepoTests {
	@Mock
	private MockDatabase mockDb = new MockDatabase();
}
