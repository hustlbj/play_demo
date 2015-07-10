import models.*;
import play.jobs.*;
import play.test.*;

@OnApplicationStart
public class Bootstrap extends Job{
	public void doJob() {
		if (User.count() == 0) {
			Fixtures.loadModels("data.yml");
		}
	}
}
