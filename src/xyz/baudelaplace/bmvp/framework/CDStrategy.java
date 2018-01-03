package xyz.baudelaplace.bmvp.framework;

import xyz.baudelaplace.bmvp.framework.views.ViewAdapter;

interface CDStrategy {
	 void run(BindingOwner ownerElement, ViewAdapter adapter);
}
