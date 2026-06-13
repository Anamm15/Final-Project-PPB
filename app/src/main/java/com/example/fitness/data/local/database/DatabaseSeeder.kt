package com.example.fitness.data.local.database

import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import at.favre.lib.crypto.bcrypt.BCrypt

object DatabaseSeeder : RoomDatabase.Callback() {
    override fun onCreate(db: SupportSQLiteDatabase) {
        super.onCreate(db)
        // MEMBERSHIP
        db.execSQL(
            "INSERT INTO membership_packages (id, name, durationDays, price, description, isActive) " +
                    "VALUES (1, 'Basic', 30, 150000.0, 'Akses gym & area cardio', 1)"
        )
        db.execSQL(
            "INSERT INTO membership_packages (id, name, durationDays, price, description, isActive) " +
                    "VALUES (2, 'Plus', 180, 750000.0, '+ kelas grup', 1)"
        )
        db.execSQL(
            "INSERT INTO membership_packages (id, name, durationDays, price, description, isActive) " +
                    "VALUES (3, 'Pro', 365, 1200000.0, '+ personal trainer', 1)"
        )

        // USER
        val adminPassword = "admin123"
        val adminHash = BCrypt.withDefaults().hashToString(12, adminPassword.toCharArray())
        db.execSQL(
            "INSERT INTO users (name, email, passwordHash, role, createdAt) VALUES (?, ?, ?, ?, ?)",
            arrayOf<Any>(
                "Admin",
                "admin@gmail.com",
                adminHash,
                "ADMIN",
                System.currentTimeMillis()
            )
        )

        val demoPassword = "user123"
        val demoHash = BCrypt.withDefaults().hashToString(12, demoPassword.toCharArray())
        db.execSQL(
            "INSERT INTO users (name, email, passwordHash, role, createdAt) VALUES (?, ?, ?, ?, ?)",
            arrayOf<Any>(
                "Demo User",
                "user@gmail.com",
                demoHash,
                "USER",
                System.currentTimeMillis()
            )
        )
        val cursor = db.query("SELECT last_insert_rowid()")
        cursor.moveToFirst()
        val demoUserId = cursor.getLong(0)
        cursor.close()

        // MEMBERSHIP -> USER
        val now = System.currentTimeMillis()
        val oneYearMs = 365L * 24 * 60 * 60 * 1000
        db.execSQL(
            "INSERT INTO memberships (userId, packageId, startDate, endDate, status) VALUES (?, ?, ?, ?, ?)",
            arrayOf<Any>(demoUserId, 3, now, now + oneYearMs, "ACTIVE")
        )

        db.execSQL(
            "INSERT INTO payments (userId, membershipId, amount, method, status, createdAt) " +
                    "VALUES (?, (SELECT id FROM memberships WHERE userId = ? ORDER BY id DESC LIMIT 1), ?, ?, ?, ?)",
            arrayOf<Any>(demoUserId, demoUserId, 1200000.0, "TRANSFER", "SUCCESS", now)
        )

        // ACTIVITY USER
        val dayMs = 24L * 60 * 60 * 1000
        fun daysAgo(n: Int) = now - n * dayMs

        val demoActivities = listOf(
            //        userId,     type,            startTime,    endTime,      poin, createdAt
            arrayOf<Any>(demoUserId, "Latihan beban", daysAgo(5), daysAgo(5), 130, daysAgo(5)),
            arrayOf<Any>(demoUserId, "Cardio",        daysAgo(4), daysAgo(4), 70,  daysAgo(4)),
            arrayOf<Any>(demoUserId, "HIIT",          daysAgo(3), daysAgo(3), 180, daysAgo(3)),
            arrayOf<Any>(demoUserId, "Yoga",          daysAgo(2), daysAgo(2), 60,  daysAgo(2)),
            arrayOf<Any>(demoUserId, "Latihan beban", daysAgo(1), daysAgo(1), 160, daysAgo(1)),
        )
        demoActivities.forEach { row ->
            db.execSQL(
                "INSERT INTO activities (userId, type, startTime, endTime, pointsEarned, createdAt) " +
                        "VALUES (?, ?, ?, ?, ?, ?)",
                row
            )
        }

        // REWARD
        db.execSQL(
            "INSERT INTO rewards (name, description, pointCost, stock, isActive) " +
                    "VALUES ('Handuk gym', 'Handuk microfiber eksklusif', 200, 10, 1)"
        )
        db.execSQL(
            "INSERT INTO rewards (name, description, pointCost, stock, isActive) " +
                    "VALUES ('Botol minum', 'Botol minum FitMember 750ml', 350, 8, 1)"
        )
        db.execSQL(
            "INSERT INTO rewards (name, description, pointCost, stock, isActive) " +
                    "VALUES ('Sesi personal trainer', '1 sesi bimbingan privat', 500, 5, 1)"
        )
        db.execSQL(
            "INSERT INTO rewards (name, description, pointCost, stock, isActive) " +
                    "VALUES ('Kaos FitMember', 'Kaos katun edisi member', 1000, 6, 1)"
        )
    }
}