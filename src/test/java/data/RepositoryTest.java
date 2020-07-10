package data;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.collection.IsIterableContainingInAnyOrder.containsInAnyOrder;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.TreeSet;

import org.hamcrest.collection.IsEmptyCollection;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.Test;

import data.dao.CategoryEntity;
import data.dao.HibernateUtil;
import data.dto.CategoryDTo;
import exception.Message;

public class RepositoryTest {
	@Test
	public final void testNoInstanciateRepository() {
		try {
			new Repository();
			fail("Exception not thrown");
		} catch (Exception aExp) {
			assert (aExp.getMessage().contains(Message.UITLITY_CLASS));
		}
	}

	@Test
	public void TestGetAllCategory() {
		CategoryDTo categoryDivers = new CategoryDTo("divers");
		CategoryDTo categorySalaire = new CategoryDTo("salaire");
		CategoryDTo categoryAlimentation = new CategoryDTo("alimentation");

		Repository.addCategory(categoryDivers);
		Repository.addCategory(categorySalaire);
		Repository.addCategory(categoryAlimentation);

		TreeSet<CategoryDTo> expected = new TreeSet<CategoryDTo>();
		expected.add(new CategoryDTo("alimentation"));
		expected.add(new CategoryDTo("divers"));
		expected.add(new CategoryDTo("salaire"));

		// 1. Test equal.
		assertEquals(expected, Repository.getAllCategory());

		// 2. If List has this value?
		assertThat(Repository.getAllCategory(), hasItems(categoryDivers));

		// 3. Check List Size
		assertThat(Repository.getAllCategory(), hasSize(3));

		assertThat(Repository.getAllCategory().size(), is(3));

		// 4. Ensure Correct order
		assertEquals(Repository.getAllCategory().first(), new CategoryDTo("alimentation"));
		assertEquals(Repository.getAllCategory().last(), new CategoryDTo("salaire"));

		// Can be any order
		assertThat(Repository.getAllCategory(),
				containsInAnyOrder(categorySalaire, categoryDivers, categoryAlimentation));

		// 5. check empty list
		assertThat(Repository.getAllCategory(), not(IsEmptyCollection.empty()));

		assertThat(new TreeSet<CategoryDTo>(), IsEmptyCollection.empty());

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

	@Test
	public void TestCountCategoryEntityWhenTableEmpty() {
		DataTest.deleteCategory();
		assertEquals(new BigInteger("0"), Repository.countCategoryEntity());
	}

	@Test
	public void TestCountCategoryEntityWhenTableNotEmpty() {
		DataTest.deleteCategory();
		// given
		Transaction tx = null;
		Session session = HibernateUtil.getSessionFactory().openSession();
		try {
			tx = session.beginTransaction();
			CategoryEntity category = new CategoryEntity("alimentation");
			CategoryEntity category2 = new CategoryEntity("salaire");
			CategoryEntity category3 = new CategoryEntity("divers");
			session.save(category);
			session.save(category2);
			session.save(category3);
			tx.commit();
		} catch (RuntimeException e) {
			if (tx != null)
				tx.rollback();
			throw e; // or display error message
		} finally {
			session.close();
		}

		assertEquals(new BigInteger("3"), Repository.countCategoryEntity());
	}

	@Test
	public void TestinitCategoriesWhenNothingToInit() {
		// given
		Repository.removeAllCategory();
		Repository.addCategories(Arrays.asList("bingo", "too", "titi"));
		// when
		Repository.initCategories();
		// then
		assertEquals(3, Repository.countCategory());
	}

	@Test
	public void TestinitCategoriesWhenTableNotEmpty() {
		// given
		Repository.removeAllCategory();
		DataTest.deleteCategory();
		Transaction tx = null;
		Session session = HibernateUtil.getSessionFactory().openSession();
		try {
			tx = session.beginTransaction();
			CategoryEntity category = new CategoryEntity("alimentation");
			CategoryEntity category2 = new CategoryEntity("divers");
			session.save(category);
			session.save(category2);
			tx.commit();
		} catch (RuntimeException e) {
			if (tx != null)
				tx.rollback();
			throw e; // or display error message
		} finally {
			session.close();
		}
		// when
		Repository.initCategories();
		// then
		assertEquals(2, Repository.countCategory());
	}

	@Test
	public void TestinitCategoriesWhenTableEmpty() {
		// given
		Repository.removeAllCategory();
		DataTest.deleteCategory();

		// when
		Repository.initCategories();
		// then
		assertEquals(40, Repository.countCategory());
	}
}
