package data;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.collection.IsIterableContainingInAnyOrder.containsInAnyOrder;
import static org.hamcrest.collection.IsIterableContainingInOrder.contains;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.hamcrest.collection.IsEmptyCollection;
import org.junit.Test;

import data.dto.CategoryDTo;

public class RepositoryTest {

	@Test
	public void TestGetAllCategory() {
		CategoryDTo categoryDivers = new CategoryDTo("divers");
		CategoryDTo categorySalaire = new CategoryDTo("salaire");
		CategoryDTo categoryAlimentation = new CategoryDTo("alimentation");

		Repository.addCategory(categorySalaire);
		Repository.addCategory(categoryAlimentation);

		List<CategoryDTo> expected = new ArrayList<CategoryDTo>(
				Arrays.asList(categoryDivers, categorySalaire, categoryAlimentation));

		// 1. Test equal.
		assertThat(Repository.getAllCategory(), is(expected));

		// 2. If List has this value?
		assertThat(Repository.getAllCategory(), hasItems(categoryDivers));

		// 3. Check List Size
		assertThat(Repository.getAllCategory(), hasSize(3));

		assertThat(Repository.getAllCategory().size(), is(3));

		// 4. List order

		// Ensure Correct order
		assertThat(Repository.getAllCategory(), contains(categoryDivers, categorySalaire, categoryAlimentation));

		// Can be any order
		assertThat(Repository.getAllCategory(),
				containsInAnyOrder(categorySalaire, categoryDivers, categoryAlimentation));

		// 5. check empty list
		assertThat(Repository.getAllCategory(), not(IsEmptyCollection.empty()));

		assertThat(new ArrayList<>(), IsEmptyCollection.empty());

	}

	// then @Test
	public void TestIsCategoryTrue() {
		// given
		CategoryDTo category = new CategoryDTo("divers");
		// when
		assertTrue(Repository.isCategory(category));

	}

	@Test
	public void TestIsCategoryFalse() {
		// given
		CategoryDTo category = new CategoryDTo("bingo");
		// when
		assertFalse(Repository.isCategory(category));

	}

}
