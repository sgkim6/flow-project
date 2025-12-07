로컬 실행 시 ddl-auto: create-drop이 적용되어 있어 고정 확장자 리스트가 없는 상태입니다.

테스트 시 아래 쿼리를 통해 초기 데이터 셋업 후 테스트 부탁드립니다.

```sql
INSERT INTO blocked_extension (name, pinned, is_valid, created_at, updated_at)
VALUES
    ('bat', true, false, NOW(), NOW()),
    ('cmd', true, false, NOW(), NOW()),
    ('com', true, false, NOW(), NOW()),
    ('cpl', true, false, NOW(), NOW()),
    ('exe', true, false, NOW(), NOW()),
    ('scr', true, false, NOW(), NOW()),
    ('js',  true, false, NOW(), NOW());
```
