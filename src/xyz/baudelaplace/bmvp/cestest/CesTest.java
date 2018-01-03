package xyz.baudelaplace.bmvp.cestest;

import java.util.concurrent.ExecutionException;

import javax.swing.SwingUtilities;

import xyz.baudelaplace.bmvp.cestest.presenters.UTMPresenter;
import xyz.baudelaplace.bmvp.framework.BMVP;
import xyz.baudelaplace.bmvp.framework.Presenter;
import xyz.baudelaplace.bmvp.framework.views.ViewAdapter;

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
