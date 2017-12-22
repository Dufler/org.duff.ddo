package org.duff.ddo.crafting.logic;

import java.lang.reflect.InvocationTargetException;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.duff.ddo.crafting.model.Item;
import org.duff.ddo.crafting.model.Property;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.widgets.Display;

public class Optimizer {

	private static final int MAX_ALLOWED_RESULTS_PER_ITEM = 3;

	private int baseScore;
	private int maxResult;

	public List<Item> findOptimum(List<Item> baseItems, List<Property> properties) {
		baseScore = calculateBaseScore(baseItems);
		maxResult = MAX_ALLOWED_RESULTS_PER_ITEM * baseItems.size();
		// Ordino le proprieta'
		properties.sort(null);
		//Trovo e posiziono tutte le property che hanno un solo possibile slot disponibile sugli oggetti
		//Rimuovo del tutto quelle che non possono essere posizionate
		boolean removed = true;
		while (removed) {
			List<Property> propertiesToRemove = new LinkedList<>();
			for (Property property : properties) {
				int avaiblePositions = 0;
				Item avaibleItem = null;
				for (Item item : baseItems) {
					if (property.getPrefix().contains(item.getSlot()) && item.getPrefix() == null) {
						avaiblePositions += 1;
						avaibleItem = item;
					}
					if (property.getSuffix().contains(item.getSlot()) && item.getSuffix() == null) {
						avaiblePositions += 1;
						avaibleItem = item;
					}
					if (property.getExtra().contains(item.getSlot()) && item.getExtra() == null) {
						avaiblePositions += 1;
						avaibleItem = item;
					}
				}
				if (avaiblePositions == 0) {
					propertiesToRemove.add(property);
					System.out.println("(Impossibile da posizionare) Rimossa la proprieta': '" + property + " perche' e' impossibile da posizionare.");
				} else if (avaiblePositions == 1) {
					if (property.getPrefix().contains(avaibleItem.getSlot())) {
						avaibleItem.setPrefix(property);
						System.out.println("(Unico slot disponibile) Rimossa la proprieta': '" + property + " e posizionata sul prefisso di " + avaibleItem.getSlot());
					} else if (property.getSuffix().contains(avaibleItem.getSlot())) {
						avaibleItem.setSuffix(property);
						System.out.println("(Unico slot disponibile) Rimossa la proprieta': '" + property + " e posizionata sul suffisso di " + avaibleItem.getSlot());
					} else if (property.getExtra().contains(avaibleItem.getSlot())) {
						avaibleItem.setExtra(property);
						System.out.println("(Unico slot disponibile) Rimossa la proprieta': '" + property + " e posizionata sull'extra di " + avaibleItem.getSlot());
					}
					propertiesToRemove.add(property);
				}
			}
			if (propertiesToRemove.isEmpty())
				removed = false;
			else
				properties.removeAll(propertiesToRemove);
		}
		//Trovo ricorsivamente l'ottimo a partire da quello che rimane
		return findOptimumRecursively(baseItems, properties);
	}

	private List<Item> findOptimumRecursively(List<Item> baseItems, List<Property> properties) {
		// Istanzio i risultati
		Set<Property> unusedProperties = new HashSet<>();
		List<List<Item>> results = new LinkedList<>();
		results.add(baseItems);

		ProgressMonitorDialog progressDialog = new ProgressMonitorDialog(Display.getCurrent().getActiveShell());
		try {
			progressDialog.run(false, true, new IRunnableWithProgress() {

				@Override
				public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
					monitor.beginTask("Calculating best equipment ...", properties.size());
					try {
						// Esamino tutte le proprieta'
						Iterator<Property> propertyIterator = properties.iterator();
						while (propertyIterator.hasNext()) {
							Property property = propertyIterator.next();
							monitor.subTask("Calculating best equipment for : '" + property.getName() + "'");
							List<List<Item>> subresults = new LinkedList<>();
							Iterator<List<Item>> iterator = results.iterator();
							// Controllo tutte le liste elaborate disponibili
							while (iterator.hasNext()) {
								// La rimuovo e trovo tutte le possibili
								// combinazioni per le proprieta' fra tutti i
								// posti disponibili rimasti.
								List<Item> items = iterator.next();
								iterator.remove();
								boolean found = false;
								for (Item item : items) {
									// Prefix
									if (property.getPrefix().contains(item.getSlot()) && (item.getPrefix() == null
											|| item.getPrefix().getPriority() < property.getPriority())) {
										// Creo una copia della lista e setto
										// sull'oggetto
										List<Item> copyList = copyList(items);
										for (Item copyItem : copyList) {
											if (copyItem.getSlot() == item.getSlot()) {
												copyItem.setPrefix(property);
												found = true;
												break;
											}
										}
										subresults.add(copyList);
									}
									// Suffix
									if (property.getSuffix().contains(item.getSlot()) && (item.getSuffix() == null
											|| item.getSuffix().getPriority() < property.getPriority())) {
										// Creo una copia della lista e setto
										// sull'oggetto
										List<Item> copyList = copyList(items);
										for (Item copyItem : copyList) {
											if (copyItem.getSlot() == item.getSlot()) {
												copyItem.setSuffix(property);
												found = true;
												break;
											}
										}
										subresults.add(copyList);
									}
									// Extra
									if (property.getExtra().contains(item.getSlot()) && (item.getExtra() == null
											|| item.getExtra().getPriority() < property.getPriority())) {
										// Creo una copia della lista e setto
										// sull'oggetto
										List<Item> copyList = copyList(items);
										for (Item copyItem : copyList) {
											if (copyItem.getSlot() == item.getSlot()) {
												copyItem.setExtra(property);
												found = true;
												break;
											}
										}
										subresults.add(copyList);
									}
								}
								if (!found) {
									unusedProperties.add(property);
								}
								subresults.add(items);
							}
							results.addAll(subresults);
							prune(results);
							monitor.worked(1);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
					monitor.done();
				}

			});
		} catch (Exception e) {
			e.printStackTrace();
		}

		// Trovo la migliore
		List<Item> bestResult = findBest(results);

		// Stampo le proprietà che non sono presenti nella soluzione ma che
		// erano richieste
		List<Property> unplaced = findUnplacedProperties(bestResult, properties);

		// Se posso ancora mettere dentro roba faccio un altro giro.
		if (unplaced.size() > unusedProperties.size())
			bestResult = findOptimumRecursively(bestResult, unplaced);

		// Stampo le proprietà che non sono riuscito a piazzare
		System.out.println("Proprieta' impossibili da piazzare: ");
		for (Property property : unusedProperties) {
			System.out.println(property.getName());
		}

		return bestResult;
	}

	private void prune(List<List<Item>> results) {
		if (results.size() > maxResult) {
			System.out.println("Trovati " + results.size() + " risultati, verranno conservati solo i " + maxResult + " migliori.");
			// Li ordino
			results.sort(new Comparator<List<Item>>() {

				@Override
				public int compare(List<Item> lista1, List<Item> lista2) {
					Integer score1 = calculateScore(lista1);
					Integer score2 = calculateScore(lista2);
					int compare = score1.compareTo(score2);
					return compare;
				}

			});
			// Taglio via i peggiori
			int index = 0;
			Iterator<List<Item>> iterator = results.iterator();
			while (iterator.hasNext()) {
				iterator.next();
				if (results.size() - index > maxResult)
					iterator.remove();
			}
		}
	}

	private int calculateBaseScore(List<Item> baseItems) {
		int result = 0;
		for (Item item : baseItems) {
			result += 6;
			if (item.getPrefix() != null)
				result -= 2;
			if (item.getSuffix() != null)
				result -= 2;
			if (item.getExtra() != null)
				result -= 2;
		}
		return result;
	}

	private List<Item> findBest(List<List<Item>> results) {
		List<Item> bestResult = null;
		int bestScore = 0;
		for (List<Item> result : results) {
			if (bestResult == null) {
				bestScore = calculateScore(result);
				bestResult = result;
			} else {
				int score = calculateScore(result);
				if (score > bestScore) {
					bestScore = score;
					bestResult = result;
				}
			}
		}
		System.out.println("Trovate " + results.size() + " possibili soluzioni.");
		System.out.println("Punteggio migliore: " + bestScore + " per : '" + bestResult + "'");
		System.out.println("Le proprietà non presenti in questa soluzione sono: ");
		return bestResult;
	}

	private List<Property> findUnplacedProperties(List<Item> items, List<Property> properties) {
		List<Property> unplaced = new LinkedList<>();
		for (Property property : properties) {
			boolean found = false;
			for (Item item : items) {
				if (item.getPrefix() != null && item.getPrefix().equals(property)) {
					found = true;
					break;
				}
				if (item.getSuffix() != null && item.getSuffix().equals(property)) {
					found = true;
					break;
				}
				if (item.getExtra() != null && item.getExtra().equals(property)) {
					found = true;
					break;
				}
			}
			if (!found)
				unplaced.add(property);
		}
		if (!unplaced.isEmpty()) {
			System.out.println("Property richieste ma non presenti nella soluzione:");
			for (Property property : unplaced)
				System.out.println(property.getName());
		} else
			System.out.println("Tutte le properties sono state posizionate!");
		return unplaced;
	}

	private List<Item> copyList(List<Item> items) {
		List<Item> copyList = new LinkedList<>();
		for (Item item : items) {
			Item copyItem = new Item(item.getSlot());
			copyItem.setPrefix(item.getPrefix());
			copyItem.setSuffix(item.getSuffix());
			copyItem.setExtra(item.getExtra());
			copyList.add(copyItem);
		}
		return copyList;
	}

	private int calculateScore(List<Item> items) {
		//System.out.println("Calcolo di: '" + items + "'");
		// Base, meno oggetti ho usato meglio è.
		int score = baseScore;
		// Per ogni oggetto guardo le proprieta' inserite e la priorita'
		// associata dall'utente.
		// Se è stato lasciato vuoto uno spazio tolgo un punto.
		for (Item item : items) {
			boolean prefix = item.getPrefix() != null;
			boolean suffix = item.getSuffix() != null;
			boolean extra = item.getExtra() != null;
			score += prefix ? item.getPrefix().getPriority() : 0;
			score += suffix ? item.getSuffix().getPriority() : 0;
			score += extra ? item.getExtra().getPriority() : 0;
			// Se è stato riempito tutto attribuisco punti extra
			if (prefix && suffix && extra)
				score += 3;
			// Se non è stato usato tolgo le penalita'
			// if (!prefix && !suffix && !extra)
			// score += 1;
		}
		//System.out.println("Punteggio: " + score);
		return score;
	}

}
