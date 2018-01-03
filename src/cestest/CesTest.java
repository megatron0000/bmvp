package cestest;

import java.util.concurrent.ExecutionException;

import javax.swing.SwingUtilities;

import cestest.presenters.UTMPresenter;
import framework.BMVP;
import framework.Presenter;
import framework.views.ViewAdapter;

public class CesTest extends BMVP {

	CesTest(Class<? extends Presenter<?>> rootPresenterClass, ViewAdapter adapter) {
		super(rootPresenterClass, adapter);
	}

	@Override
	public void configureBindings() {
		// TODO Auto-generated method stub

	}

	public static void main(String[] args) throws InterruptedException, ExecutionException {
		new CesTest(UTMPresenter.class, new ViewAdapter() {

			@Override
			public void invoke(Runnable task) {
				SwingUtilities.invokeLater(task);
			}

		}).start().exceptionally(exception -> {
			exception.printStackTrace();
			return null;
		});
	}

}
