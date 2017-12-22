package org.duff.ddo.gui.handlers;

import org.duff.ddo.gui.wizards.NewItemSetWizard;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;
public class OpenHandler {

	@Execute
	public void execute(Shell shell){
		WizardDialog dialog = new WizardDialog(shell, new NewItemSetWizard());
		dialog.open();
	}
}
