## 리팩토링 진행한 부분
- native query → JPQL
- member.uuid / member.userId 구분
- Modifying Query에 @Transactional 추가
- CatService 분리
- price → BigDecimal 타입으로 변경

## 추가 구현하고 싶은 부분
- JWT expired
- Filter에서 JWT 검증 로직 추가
- makeOrder()에서 결제 호출 이후 DB에 저장하는 부분이 있는데, 만약 DB에서 실패한다면 호출한 결제 내역도 취소되어야 하므로 TransactionalEventListener 적용 검토
- 페이징 처리
- logback 설정 추가