package com.usc.cems.di;

import com.usc.cems.data.local.AppDatabase;
import com.usc.cems.data.local.PlaceholderDao;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.Provider;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

@ScopeMetadata("javax.inject.Singleton")
@QualifierMetadata
@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava",
    "cast",
    "deprecation",
    "nullness:initialization.field.uninitialized"
})
public final class DatabaseModule_ProvidePlaceholderDaoFactory implements Factory<PlaceholderDao> {
  private final Provider<AppDatabase> databaseProvider;

  private DatabaseModule_ProvidePlaceholderDaoFactory(Provider<AppDatabase> databaseProvider) {
    this.databaseProvider = databaseProvider;
  }

  @Override
  public PlaceholderDao get() {
    return providePlaceholderDao(databaseProvider.get());
  }

  public static DatabaseModule_ProvidePlaceholderDaoFactory create(
      Provider<AppDatabase> databaseProvider) {
    return new DatabaseModule_ProvidePlaceholderDaoFactory(databaseProvider);
  }

  public static PlaceholderDao providePlaceholderDao(AppDatabase database) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.providePlaceholderDao(database));
  }
}
