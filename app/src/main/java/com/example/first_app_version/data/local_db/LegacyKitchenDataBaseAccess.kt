package com.example.first_app_version.data.local_db

/**
 * Transitional compatibility shim.
 *
 * Once you refactor all repositories to use injected DAOs (recommended),
 * you should delete this file and stop calling KitchenDataBase.getDataBase(...).
 *
 * Why it exists:
 * - Your codebase currently calls KitchenDataBase.getDataBase(application)
 * - After moving DB creation to Hilt, that pattern should be removed
 *
 * This file intentionally has no implementation to force compile errors
 * in places that still use the legacy singleton so you can migrate them.
 */
@Deprecated(
    message = "KitchenDataBase singleton access has been replaced by Hilt. Inject KitchenDataBase/DAOs instead."
)
private object LegacyKitchenDataBaseAccess