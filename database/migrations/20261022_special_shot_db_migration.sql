-- ============================================================
-- MH Sunbreak Simulator
-- 特殊射撃強化 / 竜の一矢 / 起爆竜弾 DB再現用マイグレーション
-- PostgreSQL
--
-- 対応内容:
-- 1. skill_effect_state.special_shot_mul を追加
-- 2. 特殊射撃強化(skill_id=95) Lv1/Lv2 の倍率を設定
-- 3. preset_bow_setting.bow_attack_type を追加
-- 4. preset_bowgun_setting に起爆竜弾保存用カラムを追加
-- 5. 起爆竜弾では通常弾情報を持たないため ammo_type / ammo_level をNULL許可
--
-- 既存DBに対して再実行しても問題が起きにくいよう、
-- ADD COLUMN IF NOT EXISTS と冪等なUPDATEを使用する。
-- ============================================================

BEGIN;


-- ============================================================
-- 1. 特殊射撃強化倍率
-- ============================================================

ALTER TABLE skill_effect_state
    ADD COLUMN IF NOT EXISTS special_shot_mul NUMERIC(10, 5);


-- 特殊射撃強化(skill_id=95)
-- Lv1 = 1.10
-- Lv2 = 1.20
UPDATE skill_effect_state es
SET special_shot_mul =
    CASE sl.level
        WHEN 1 THEN 1.10000
        WHEN 2 THEN 1.20000
        ELSE es.special_shot_mul
    END
FROM skill_level sl
WHERE sl.skill_level_id = es.skill_level_id
  AND sl.skill_id = 95
  AND sl.level IN (1, 2);


-- ============================================================
-- 2. 弓プリセット
-- ============================================================

ALTER TABLE preset_bow_setting
    ADD COLUMN IF NOT EXISTS bow_attack_type VARCHAR(32)
        NOT NULL
        DEFAULT 'NORMAL';


-- ============================================================
-- 3. ボウガンプリセット
-- ============================================================

ALTER TABLE preset_bowgun_setting
    ADD COLUMN IF NOT EXISTS bowgun_attack_type VARCHAR(32)
        NOT NULL
        DEFAULT 'NORMAL_AMMO';

ALTER TABLE preset_bowgun_setting
    ADD COLUMN IF NOT EXISTS wyvernblast_explosion_size VARCHAR(32);

-- 起爆竜弾では通常弾の種類・Lvを保存しないためNULLを許可する。
ALTER TABLE preset_bowgun_setting
    ALTER COLUMN ammo_type DROP NOT NULL;

ALTER TABLE preset_bowgun_setting
    ALTER COLUMN ammo_level DROP NOT NULL;


COMMIT;


-- ============================================================
-- 確認用SELECT
-- ============================================================

-- 特殊射撃強化 Lv1 / Lv2
SELECT
    sl.skill_id,
    sl.level,
    es.state_code,
    es.special_shot_mul
FROM skill_effect_state es
INNER JOIN skill_level sl
    ON sl.skill_level_id = es.skill_level_id
WHERE sl.skill_id = 95
ORDER BY
    sl.level,
    es.state_code;


-- 弓プリセット用カラム確認
SELECT
    column_name,
    data_type,
    is_nullable,
    column_default
FROM information_schema.columns
WHERE table_schema = 'public'
  AND table_name = 'preset_bow_setting'
  AND column_name = 'bow_attack_type';


-- ボウガンプリセット用カラム確認
SELECT
    column_name,
    data_type,
    is_nullable,
    column_default
FROM information_schema.columns
WHERE table_schema = 'public'
  AND table_name = 'preset_bowgun_setting'
  AND column_name IN (
      'bowgun_attack_type',
      'wyvernblast_explosion_size',
      'ammo_type',
      'ammo_level'
  )
ORDER BY column_name;
