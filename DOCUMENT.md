làm thế nào để phần biêtj được đâu là 
### cuộc gọi 
1. cuộc gọi nhỡ 
2. không phải thư rác 
3. spam 
4. doanh nghiệp được xác minh 
5. cuộc gọi đi 
6. cuộc gọi đến
----------------------
### tin nhắn 
1. hộn thư đến
2. chưa đọc 
3. spam
-----------------------
### rule
Không có API riêng thì vẫn làm được **khoảng 2/3** số loại. Android có sẵn dữ liệu cho phần đó, cộng thêm vài thư viện miễn phí chạy trên máy. Phần không làm được là **doanh nghiệp được xác minh** và **spam chính xác như Truecaller**, vì hai thứ này cần cơ sở dữ liệu cộng đồng mà chỉ họ có.

## Tổng quan

| Loại | Làm được không? | Dùng gì |
|---|---|---|
| Cuộc gọi đến / đi / nhỡ | ✅ Chính xác 100% | `CallLog.Calls.TYPE` (có sẵn trong Android) |
| Cuộc gọi bị chặn / bị từ chối | ✅ | `CallLog.Calls.TYPE` = `BLOCKED` (6), `REJECTED` (5) |
| Cuộc gọi spam / không spam | ⚠️ Chỉ ước lượng | Luật tự viết + **libphonenumber** + danh sách chặn |
| Doanh nghiệp được xác minh | ❌ | Không có nguồn miễn phí |
| SMS hộp thư đến / chưa đọc | ✅ Chính xác 100% | `Telephony.Sms.TYPE`, `Telephony.Sms.READ` |
| SMS spam | ⚠️ Làm khá tốt | Luật tự viết + model **TensorFlow Lite** chạy trên máy |

## 1. Cuộc gọi: Android có sẵn
```kotlin
val c = contentResolver.query(CallLog.Calls.CONTENT_URI,
    arrayOf(CallLog.Calls.NUMBER, CallLog.Calls.TYPE, CallLog.Calls.DURATION, CallLog.Calls.CACHED_NAME),
    null, null, "${CallLog.Calls.DATE} DESC")
when (type) {
    CallLog.Calls.INCOMING_TYPE -> "Đến"        // 1
    CallLog.Calls.OUTGOING_TYPE -> "Đi"         // 2
    CallLog.Calls.MISSED_TYPE   -> "Nhỡ"        // 3
    CallLog.Calls.REJECTED_TYPE -> "Từ chối"    // 5
    CallLog.Calls.BLOCKED_TYPE  -> "Đã chặn"    // 6, tính vào nhóm spam
}
```
Cần quyền `READ_CALL_LOG`.

## 2. Spam cuộc gọi khi không có API: ghép nhiều tín hiệu
- **[libphonenumber](https://github.com/google/libphonenumber)** của Google (miễn phí, chạy offline). Dùng dependency `com.googlecode.libphonenumber:libphonenumber`. Thư viện cho biết số có hợp lệ không và thuộc loại nào: `MOBILE`, `FIXED_LINE`, `TOLL_FREE`, `PREMIUM_RATE`, `VOIP`, `SHARED_COST`. Số không hợp lệ, số `PREMIUM_RATE` hoặc `VOIP` thì nghi ngờ cao.
- **Danh bạ** (`ContactsContract.PhoneLookup`): số có trong danh bạ thì coi là **không spam**.
- **`BlockedNumberContract.isBlocked()`**: số người dùng đã chặn trên hệ thống thì coi là **spam**.
- **Hành vi trong nhật ký cuộc gọi**: số lạ, gọi nhiều lần trong ngày, cuộc gọi rất ngắn hoặc luôn bị nhỡ hay từ chối thì nghi ngờ.
- **Danh sách spam riêng của app**: người dùng bấm "báo spam" thì lưu số vào Room. Đây là bản thu nhỏ của cách Truecaller làm.
- **`CallScreeningService`**: cho phép app kiểm tra số **ngay khi điện thoại đổ chuông**, rồi chặn hoặc hiện cảnh báo. Cần role `ROLE_CALL_SCREENING`.

Gợi ý cách chấm: cộng điểm từ các tín hiệu trên. Điểm vượt ngưỡng thì xếp vào "Spam". Số có trong danh bạ thì luôn là "Không phải thư rác".

## 3. Tin nhắn
```kotlin
// Hộp thư đến
Telephony.Sms.TYPE = Telephony.Sms.MESSAGE_TYPE_INBOX   // 1
// Chưa đọc
Telephony.Sms.READ = 0
```
Để nhận diện **SMS spam** trên máy:
- **Luật tự viết**: nội dung có link lạ, từ khoá như "trúng thưởng", "vay", "OTP", "khoá tài khoản", "click"; người gửi không có trong danh bạ.
- **TensorFlow Lite / ML Kit**: tự train một model phân loại văn bản (spam / không spam) trên tập SMS mẫu, rồi chạy trên máy. Truecaller cũng làm đúng như vậy với SMS (file `core3`). Bạn sẽ phải tự tìm hoặc tự gom tập dữ liệu tin nhắn **tiếng Việt** để train.

Cần quyền `READ_SMS`. Muốn đánh dấu đã đọc hoặc xoá tin thì app phải là ứng dụng SMS mặc định.

## Không làm được nếu không có dữ liệu riêng
- **Doanh nghiệp được xác minh**: Truecaller tự xác minh doanh nghiệp rồi cấp huy hiệu `VERIFIED_BUSINESS`. Không có thư viện miễn phí nào cung cấp dữ liệu này.
- **Spam chính xác cho số lạ chưa từng gọi**: muốn có thì cần cơ sở dữ liệu báo cáo spam của nhiều người dùng, tức là phải tự dựng server.

Bạn có muốn mình viết luôn module `CallClassifier` và `SmsClassifier` trong `base-app` theo cách trên (CallLog + libphonenumber + danh bạ + luật chấm điểm) không?
-------------------------
### premission 
Dưới đây là danh sách các quyền trong luồng onboarding của Truecaller, giữ nguyên thông tin và thứ tự như bạn đã tổng hợp:

### 4 nhóm quyền runtime xin lần lượt theo hộp thoại hệ thống

* **1. "Truy cập vào nhật ký cuộc gọi"** — Nhóm quyền `CALL_LOG`: `READ_CALL_LOG` (có thể kèm `WRITE_CALL_LOG`)
* **2. "Truy cập vào danh bạ"** — Nhóm quyền `CONTACTS`: `READ_CONTACTS` (có thể kèm `WRITE_CONTACTS`, `GET_ACCOUNTS`)
* **3. "Thực hiện và quản lý cuộc gọi điện thoại"** — Nhóm quyền `PHONE`: `READ_PHONE_STATE`, `CALL_PHONE`, `READ_PHONE_NUMBERS`, có thể có `ANSWER_PHONE_CALLS`
* **4. "Gửi và xem tin nhắn SMS"** — Nhóm quyền `SMS`: `READ_SMS`, `RECEIVE_SMS`, `SEND_SMS`



### Nội dung giải thích ở hai màn hình đầu (chưa xin quyền)

* **Cuộc gọi điện thoại:** Xác minh số và hiện tên người gọi (caller ID) cho số lạ
* **Liên hệ:** Hiện tên người gọi và không đánh dấu nhầm người quen là spam; app ghi rõ danh bạ không được chia sẻ
* **Nhật ký cuộc gọi:** Nhận diện các số lạ trong lịch sử cuộc gọi
* **Tin nhắn SMS:** Lọc spam và phân loại tin nhắn; app ghi rõ nội dung tin nhắn ở lại trên máy
* **Quyền tùy chọn:** Ảnh, máy ảnh, Bluetooth, micrô, vị trí — chỉ xin khi dùng tính năng liên quan, không xin ở bước này
-------------------------
claude :
Đọc được rồi. Mình đã cài Java 21 và jadx 1.5.7 vào ~/.local/opt/ (không cần sudo, đã kiểm tra checksum của JDK), rồi dịch ngược APK ra 57.004 file Java trong thư mục decompiled/, mất khoảng 4 phút.

Thông tin chung
Phiên bản 26.45.5, chạy từ Android 8.0 trở lên, nhắm tới Android 17 (SDK 37).
App gồm 362 màn hình (activity), 52 service chạy nền, 74 receiver và 18 provider.
Phần code riêng của Truecaller nằm trong com/truecaller/, chia thành 177 module. Ví dụ: callerid (nhận diện người gọi), blocking (chặn), topspammers (danh sách số spam), whoviewedme / whosearchedforme (ai xem hoặc tìm bạn), call_assistant (trợ lý AI nghe máy), ai_voice_detection (phát hiện giọng AI), familyprotect (bảo vệ gia đình), urlinspection (kiểm tra link lừa đảo).
Quyền app xin (rất rộng)
Cuộc gọi: đọc và ghi nhật ký cuộc gọi, gọi điện, tự trả lời cuộc gọi, xem cuộc gọi đi.
SMS/MMS: đọc, gửi, nhận và ghi tin nhắn (vì app có thể làm ứng dụng nhắn tin mặc định).
Danh bạ: đọc, ghi danh bạ và đọc danh sách tài khoản trên máy.
Thiết bị: micro (ghi âm cuộc gọi), camera, Bluetooth, NFC, nhận diện hoạt động thể chất, hiển thị đè lên app khác, đọc thông báo của app khác.
Quảng cáo: mã quảng cáo (AD_ID), Topics API và Attribution API của Google.
SDK bên thứ ba được nhúng
Quảng cáo (rất nhiều): Facebook Audience Network, Google Ads, AppLovin/ironSource, Unity Ads, Vungle, InMobi, Chartboost, Mintegral (mbridge), Moloco, Fyber/Digital Turbine, Criteo, Amazon, Appnext.
Theo dõi và marketing: AppsFlyer, CleverTap.
Thanh toán: Razorpay, Stripe, Google Play Billing.
Hỗ trợ khách hàng: Freshchat.
Server

Các địa chỉ server được ghép trong code, nằm ở KnownEndpoints.java, theo dạng <dịch vụ>-eu.truecaller.com cho người dùng châu Âu và <dịch vụ>-noneu.truecaller.com cho các nước khác. Có 69 dịch vụ, trong đó có:

search5: tra cứu số điện thoại.
contact-upload4, phonebook5: tải danh bạ và gợi ý tên lên server.
batchlogging4: gửi log và số liệu thống kê.
ads-auction-engine, ads-router, ads-segment-profile: đấu giá quảng cáo và phân nhóm người dùng.
presence-grpc, lastactivity: trạng thái online của người dùng.
message-classifier-patterns, url-inspection: phân loại tin nhắn và kiểm tra link.

Một ví dụ cụ thể: trong NameSuggestionUploadWork.java, app gửi số điện thoại + tên + loại + nguồn lên /v1/upload/nameSuggestion. Đây là một phần cách Truecaller xây dựng cơ sở dữ liệu tên từ dữ liệu người dùng gửi lên.
------------
###
# CLAUDE.md — Phân loại cuộc gọi & tin nhắn (tham chiếu Truecaller 26.45.5)

Project `base-app` đang tham khảo Truecaller 26.45.5 (dịch ngược bằng jadx). Mã nguồn dịch ngược nằm ở
`~/Desktop/Truecaller_+Spam+Call+Blocker_26.45.5_APKPure/decompiled/sources/`.
Các file ghi chú trong repo: `DOCUMENT.md` (tổng quan APK), `core` (`KnownEndpoints`: server),
`core2` (`NameSuggestionUploadWork`: gửi tên/số lên server), `core3` (`ModelQAFragmentViewModel`: model AI chấm spam/fraud cho SMS),
`core4` (các quyền runtime xin lúc onboarding).

Mức độ tin cậy dùng trong file này:
- **[code]**: đọc thẳng từ SQL hoặc enum trong mã dịch ngược.
- **[suy luận]**: suy ra từ tên cột hoặc tên enum, chưa thấy đoạn code áp dụng (R8 đã inline/xáo tên). Cần kiểm chứng trước khi coi là chắc chắn.

---

## 1. Cuộc gọi

### Nguồn dữ liệu
- Truecaller chép nhật ký cuộc gọi hệ thống (`CallLog.Calls`) vào bảng riêng **`history`** (provider `history_with_aggregated_contact…`).
  Cột chính (`defpackage/wq70.java`, `d360.java`): `normalized_number, raw_number, type, action, call_log_id, timestamp, duration, filter_source, ringing_duration, tc_flag, subscription_id, is_read, new`.
- Thông tin người gọi (tên, spam, huy hiệu) nằm ở bảng **`aggregated_contact`**, được join vào `history`.
  Cột liên quan: `contact_spam_score`, `contact_spam_type`, `contact_badges`, `contact_source`.
- Danh sách bộ lọc của màn hình Lịch sử cuộc gọi: `com/truecaller/calllog/api/FilterType.java`
  `NONE, INCOMING, OUTGOING, MISSED, BLOCKED_AND_SPAM, RECORDINGS, VERIFIED_BUSINESS, NOT_SPAM, VOICEMAIL, ASSISTANT, …`

### Giá trị `history.type` (trùng với `CallLog.Calls.TYPE` của Android) [code]
| `type` | Nghĩa |
|---|---|
| 1 | Cuộc gọi đến (`INCOMING_TYPE`) |
| 2 | Cuộc gọi đi (`OUTGOING_TYPE`) |
| 3 | Cuộc gọi nhỡ (`MISSED_TYPE`) |

Câu query gốc (`defpackage/m3g.java`, khoảng dòng 795–813):
```sql
-- Tất cả
type IN (1,2,3)
-- Đã chặn
type IN (1,2,3) AND action IN (1, 3)
-- Đến / Đi / Nhỡ: loại bỏ cuộc gọi đã bị chặn rồi lọc theo type
type IN (1,2,3) AND action!=1 AND action!=7 AND action!=3 AND type=?   -- ? = 1 | 2 | 3
```
`action` là hành động Truecaller đã làm với cuộc gọi. `1` và `3` là các kiểu **chặn** [code]. `7` cũng bị loại khỏi các tab thường, nhiều khả năng là chặn hoặc từ chối tự động [suy luận].
`filter_source` cho biết lý do chặn, ví dụ `NUMBER_SERIES`, `UNVERIFIED_SERVICE_CALL`, `INDIAN_REGISTERED_TELEMARKETER` (`defpackage/og90.java`).

### Bảng phân biệt

| # | Loại | Điều kiện nhận diện |
|---|---|---|
| 1 | **Cuộc gọi nhỡ** | `type = 3` và `action NOT IN (1,3,7)`. Cuộc gọi bị chặn không tính là nhỡ. [code] |
| 2 | **Không phải thư rác** (`NOT_SPAM`) | Người gọi **không** bị đánh dấu spam: `contact_spam_type IS NULL` (hoặc khác `'SPAMMER'`), `contact_spam_score` = 0/NULL, và cuộc gọi không bị chặn (`action NOT IN (1,3,7)`). [suy luận] |
| 3 | **Spam** (`BLOCKED_AND_SPAM`) | Có **một** trong các điều kiện: `action IN (1,3)` (đã chặn) [code], hoặc người gọi có `contact_spam_type = 'SPAMMER'`. Cột này được set khi `contact_spam_score > 0` (`defpackage/k3w.java:471`) [code]. Số nằm trong bảng `topspammers` cũng tính là spam. |
| 4 | **Doanh nghiệp được xác minh** (`VERIFIED_BUSINESS`) | Dữ liệu caller-ID từ server trả về huy hiệu `Badge.VERIFIED_BUSINESS = 7` (`com/truecaller/api/services/callerid/v1/model/Badge.java`). Huy hiệu được lưu vào `aggregated_contact.contact_badges`. [code cho enum; cách lọc là suy luận] |
| 5 | **Cuộc gọi đi** | `type = 2` và `action NOT IN (1,3,7)`. [code] |
| 6 | **Cuộc gọi đến** | `type = 1` và `action NOT IN (1,3,7)`. Không gồm cuộc gọi nhỡ (`3`). [code] |

Ghi chú:
- Một cuộc gọi có thể thuộc **nhiều** nhóm, ví dụ vừa "đến" vừa "doanh nghiệp xác minh". Các nhóm 1, 5, 6 phân theo **hướng** cuộc gọi; các nhóm 2, 3, 4 phân theo **danh tính người gọi**.
- Spam và không spam là hai nhóm **loại trừ nhau**. Doanh nghiệp được xác minh thì không bao giờ là spam.
- Điều kiện `tc_flag=0 AND duration=0 AND call_log_id IS NULL …` dùng để dọn bản ghi rác (`INVALID_ENTRIES`), không phải một tab hiển thị cho người dùng.

### Cách làm trong `base-app` (không có server Truecaller)
```kotlin
when (cursor.getInt(CallLog.Calls.TYPE)) {
    CallLog.Calls.INCOMING_TYPE -> Incoming     // 1
    CallLog.Calls.OUTGOING_TYPE -> Outgoing     // 2
    CallLog.Calls.MISSED_TYPE   -> Missed       // 3
    CallLog.Calls.REJECTED_TYPE,                // 5
    CallLog.Calls.BLOCKED_TYPE  -> Blocked      // 6, tính vào nhóm Spam
}
```
- Spam, không spam và doanh nghiệp xác minh **không có** trong CallLog của Android. App phải tự có nguồn dữ liệu: danh sách chặn hoặc spam cục bộ, API tra số, hoặc `CallScreeningService`.
- Quyền cần có: `READ_CALL_LOG`, `READ_CONTACTS`, `READ_PHONE_STATE` (xem `core4`).

---

## 2. Tin nhắn

### Nguồn dữ liệu
- Bảng **`msg_messages`**: mỗi tin có `category`, `read`/`seen`, `transport`.
- Bảng **`msg_conversations`**: `is_spam`, `top_spammer_count`, `blacklist_count`, `white_list_count`, `phonebook_count`, `has_outgoing_messages`, `archived_date`, `marked_unread`, `split_criteria`.
- Bảng **`msg_thread_stats`**: mỗi cặp (hội thoại, `filter`) có `unread_messages_count`, `latest_message_id`. Cột `filter` lấy từ `msg_messages.category`.
- Danh sách tab: `com/truecaller/messaging/data/types/InboxTab.java`
  `PERSONAL(1), OTHERS(2), BUSINESS(1), SPAM(3), PROMOTIONAL(4)`. Số trong ngoặc là giá trị `filter`/`category`.
- Chip lọc ở UI mới: `MessageFilterType` = `UNREAD, INBOX, BILL, DELIVERY, TRAVEL, OTP, TRANSACTION, SPAM, OFFERS, NON_DMA_INBOX`.

### Bảng phân biệt

| # | Loại | Điều kiện nhận diện |
|---|---|---|
| 1 | **Hộp thư đến** (Inbox) | `msg_thread_stats.filter = 1` và hội thoại **không** lưu trữ, **không** bị chặn: `archived_date = 0 AND blacklist_count = 0` (`defpackage/mw6.java:215`). [code] |
| 2 | **Chưa đọc** | `unread_messages_count > 0` **hoặc** người dùng tự đánh dấu chưa đọc (`marked_unread = 1`). Truecaller chỉ đếm khi `filter = 1` và hội thoại không lưu trữ hay bị chặn (`mw6.java:215`). [code] Ở cấp từng tin: cột `read = 0`. |
| 3 | **Spam** | `msg_thread_stats.filter = 3` (`InboxTab.SPAM`), **hoặc** `msg_conversations.is_spam = 1`, **hoặc** `top_spammer_count = 1`. `is_spam` được tính từ `aggregated_contact.contact_spam_type` của người gửi (`defpackage/e6w.java:134`). [code] |

Thứ tự ưu tiên khi xếp hội thoại (`split_criteria`, `defpackage/e6w.java:135`) [code]:
1. Có `white_list_count`, `blacklist_count`, `has_outgoing_messages`, là nhóm chat, **hoặc** là spam / top spammer (với hội thoại không phải nhóm): `split_criteria = 0`.
2. Có lịch sử cuộc gọi (`history_events_count`), có trong danh bạ (`phonebook_count`), hoặc là nhóm (`type NOT IN (2,3)`): `split_criteria = 2`.
3. Còn lại, tức người lạ chưa từng liên hệ: `split_criteria = 1` (thường vào tab "Khác").

Ngoài luật trên, nội dung tin nhắn còn được chấm bằng **model AI chạy trên máy** (`core3`): `spam-categoriser` (spam) và `content-fraud-alpha-version[-za]` (lừa đảo). Model trả về `Spam score` và `Ham score`. Mẫu luật lấy từ server `message-classifier-patterns` (`core`).

### Cách làm trong `base-app` (dùng `Telephony.Sms`)
```kotlin
// Hộp thư đến
Telephony.Sms.TYPE == Telephony.Sms.MESSAGE_TYPE_INBOX   // 1
// Chưa đọc
Telephony.Sms.READ == 0
// Spam: Android không có cờ này. App tự quyết bằng:
//   số người gửi nằm trong danh sách spam/chặn, hoặc
//   người gửi không có trong danh bạ + nội dung khớp luật/model, hoặc
//   BlockedNumberContract.isBlocked(context, address)
```
Quyền cần có: `READ_SMS`, `RECEIVE_SMS`. Muốn đánh dấu đã đọc hoặc xoá tin thì app phải là **ứng dụng SMS mặc định** (`ROLE_SMS`).

---

## Quy ước khi làm việc trong repo này
- Khi trích dẫn từ mã dịch ngược, ghi rõ đường dẫn file và dòng, và đánh dấu [code] hoặc [suy luận].
- Tên class trong `defpackage/` (ví dụ `m3g`, `mw6`) là tên đã bị xáo (obfuscate) và **sẽ đổi giữa các phiên bản**. Khi tìm lại, tìm theo chuỗi SQL, đừng tìm theo tên class.


================================
- # CLAUDE.md — Phân loại cuộc gọi & tin nhắn (tham chiếu Truecaller 26.45.5)

Project `base-app` đang tham khảo Truecaller 26.45.5 (dịch ngược bằng jadx). Mã nguồn dịch ngược nằm ở
`~/Desktop/Truecaller_+Spam+Call+Blocker_26.45.5_APKPure/decompiled/sources/`.
Các file ghi chú trong repo: `DOCUMENT.md` (tổng quan APK), `core` (`KnownEndpoints`: server),
`core2` (`NameSuggestionUploadWork`: gửi tên/số lên server), `core3` (`ModelQAFragmentViewModel`: model AI chấm spam/fraud cho SMS),
`core4` (các quyền runtime xin lúc onboarding).

Mức độ tin cậy dùng trong file này:
- **[code]**: đọc thẳng từ SQL hoặc enum trong mã dịch ngược.
- **[suy luận]**: suy ra từ tên cột hoặc tên enum, chưa thấy đoạn code áp dụng (R8 đã inline/xáo tên). Cần kiểm chứng trước khi coi là chắc chắn.

---

## 1. Cuộc gọi

### Nguồn dữ liệu
- Truecaller chép nhật ký cuộc gọi hệ thống (`CallLog.Calls`) vào bảng riêng **`history`** (provider `history_with_aggregated_contact…`).
  Cột chính (`defpackage/wq70.java`, `d360.java`): `normalized_number, raw_number, type, action, call_log_id, timestamp, duration, filter_source, ringing_duration, tc_flag, subscription_id, is_read, new`.
- Thông tin người gọi (tên, spam, huy hiệu) nằm ở bảng **`aggregated_contact`**, được join vào `history`.
  Cột liên quan: `contact_spam_score`, `contact_spam_type`, `contact_badges`, `contact_source`.
- Danh sách bộ lọc của màn hình Lịch sử cuộc gọi: `com/truecaller/calllog/api/FilterType.java`
  `NONE, INCOMING, OUTGOING, MISSED, BLOCKED_AND_SPAM, RECORDINGS, VERIFIED_BUSINESS, NOT_SPAM, VOICEMAIL, ASSISTANT, …`

### Giá trị `history.type` (trùng với `CallLog.Calls.TYPE` của Android) [code]
| `type` | Nghĩa |
|---|---|
| 1 | Cuộc gọi đến (`INCOMING_TYPE`) |
| 2 | Cuộc gọi đi (`OUTGOING_TYPE`) |
| 3 | Cuộc gọi nhỡ (`MISSED_TYPE`) |

Câu query gốc (`defpackage/m3g.java`, khoảng dòng 795–813):
```sql
-- Tất cả
type IN (1,2,3)
-- Đã chặn
type IN (1,2,3) AND action IN (1, 3)
-- Đến / Đi / Nhỡ: loại bỏ cuộc gọi đã bị chặn rồi lọc theo type
type IN (1,2,3) AND action!=1 AND action!=7 AND action!=3 AND type=?   -- ? = 1 | 2 | 3
```
`action` là hành động Truecaller đã làm với cuộc gọi. `1` và `3` là các kiểu **chặn** [code]. `7` cũng bị loại khỏi các tab thường, nhiều khả năng là chặn hoặc từ chối tự động [suy luận].
`filter_source` cho biết lý do chặn, ví dụ `NUMBER_SERIES`, `UNVERIFIED_SERVICE_CALL`, `INDIAN_REGISTERED_TELEMARKETER` (`defpackage/og90.java`).

### Bảng phân biệt

| # | Loại | Điều kiện nhận diện |
|---|---|---|
| 1 | **Cuộc gọi nhỡ** | `type = 3` và `action NOT IN (1,3,7)`. Cuộc gọi bị chặn không tính là nhỡ. [code] |
| 2 | **Không phải thư rác** (`NOT_SPAM`) | Người gọi **không** bị đánh dấu spam: `contact_spam_type IS NULL` (hoặc khác `'SPAMMER'`), `contact_spam_score` = 0/NULL, và cuộc gọi không bị chặn (`action NOT IN (1,3,7)`). [suy luận] |
| 3 | **Spam** (`BLOCKED_AND_SPAM`) | Có **một** trong các điều kiện: `action IN (1,3)` (đã chặn) [code], hoặc người gọi có `contact_spam_type = 'SPAMMER'`. Cột này được set khi `contact_spam_score > 0` (`defpackage/k3w.java:471`) [code]. Số nằm trong bảng `topspammers` cũng tính là spam. |
| 4 | **Doanh nghiệp được xác minh** (`VERIFIED_BUSINESS`) | Dữ liệu caller-ID từ server trả về huy hiệu `Badge.VERIFIED_BUSINESS = 7` (`com/truecaller/api/services/callerid/v1/model/Badge.java`). Huy hiệu được lưu vào `aggregated_contact.contact_badges`. [code cho enum; cách lọc là suy luận] |
| 5 | **Cuộc gọi đi** | `type = 2` và `action NOT IN (1,3,7)`. [code] |
| 6 | **Cuộc gọi đến** | `type = 1` và `action NOT IN (1,3,7)`. Không gồm cuộc gọi nhỡ (`3`). [code] |

Ghi chú:
- Một cuộc gọi có thể thuộc **nhiều** nhóm, ví dụ vừa "đến" vừa "doanh nghiệp xác minh". Các nhóm 1, 5, 6 phân theo **hướng** cuộc gọi; các nhóm 2, 3, 4 phân theo **danh tính người gọi**.
- Spam và không spam là hai nhóm **loại trừ nhau**. Doanh nghiệp được xác minh thì không bao giờ là spam.
- Điều kiện `tc_flag=0 AND duration=0 AND call_log_id IS NULL …` dùng để dọn bản ghi rác (`INVALID_ENTRIES`), không phải một tab hiển thị cho người dùng.

### Cách làm trong `base-app` (không có server Truecaller)
```kotlin
when (cursor.getInt(CallLog.Calls.TYPE)) {
    CallLog.Calls.INCOMING_TYPE -> Incoming     // 1
    CallLog.Calls.OUTGOING_TYPE -> Outgoing     // 2
    CallLog.Calls.MISSED_TYPE   -> Missed       // 3
    CallLog.Calls.REJECTED_TYPE,                // 5
    CallLog.Calls.BLOCKED_TYPE  -> Blocked      // 6, tính vào nhóm Spam
}
```
- Spam, không spam và doanh nghiệp xác minh **không có** trong CallLog của Android. App phải tự có nguồn dữ liệu: danh sách chặn hoặc spam cục bộ, API tra số, hoặc `CallScreeningService`.
- Quyền cần có: `READ_CALL_LOG`, `READ_CONTACTS`, `READ_PHONE_STATE` (xem `core4`).

---

## 2. Tin nhắn

### Nguồn dữ liệu
- Bảng **`msg_messages`**: mỗi tin có `category`, `read`/`seen`, `transport`.
- Bảng **`msg_conversations`**: `is_spam`, `top_spammer_count`, `blacklist_count`, `white_list_count`, `phonebook_count`, `has_outgoing_messages`, `archived_date`, `marked_unread`, `split_criteria`.
- Bảng **`msg_thread_stats`**: mỗi cặp (hội thoại, `filter`) có `unread_messages_count`, `latest_message_id`. Cột `filter` lấy từ `msg_messages.category`.
- Danh sách tab: `com/truecaller/messaging/data/types/InboxTab.java`
  `PERSONAL(1), OTHERS(2), BUSINESS(1), SPAM(3), PROMOTIONAL(4)`. Số trong ngoặc là giá trị `filter`/`category`.
- Chip lọc ở UI mới: `MessageFilterType` = `UNREAD, INBOX, BILL, DELIVERY, TRAVEL, OTP, TRANSACTION, SPAM, OFFERS, NON_DMA_INBOX`.

### Bảng phân biệt

| # | Loại | Điều kiện nhận diện |
|---|---|---|
| 1 | **Hộp thư đến** (Inbox) | `msg_thread_stats.filter = 1` và hội thoại **không** lưu trữ, **không** bị chặn: `archived_date = 0 AND blacklist_count = 0` (`defpackage/mw6.java:215`). [code] |
| 2 | **Chưa đọc** | `unread_messages_count > 0` **hoặc** người dùng tự đánh dấu chưa đọc (`marked_unread = 1`). Truecaller chỉ đếm khi `filter = 1` và hội thoại không lưu trữ hay bị chặn (`mw6.java:215`). [code] Ở cấp từng tin: cột `read = 0`. |
| 3 | **Spam** | `msg_thread_stats.filter = 3` (`InboxTab.SPAM`), **hoặc** `msg_conversations.is_spam = 1`, **hoặc** `top_spammer_count = 1`. `is_spam` được tính từ `aggregated_contact.contact_spam_type` của người gửi (`defpackage/e6w.java:134`). [code] |

Thứ tự ưu tiên khi xếp hội thoại (`split_criteria`, `defpackage/e6w.java:135`) [code]:
1. Có `white_list_count`, `blacklist_count`, `has_outgoing_messages`, là nhóm chat, **hoặc** là spam / top spammer (với hội thoại không phải nhóm): `split_criteria = 0`.
2. Có lịch sử cuộc gọi (`history_events_count`), có trong danh bạ (`phonebook_count`), hoặc là nhóm (`type NOT IN (2,3)`): `split_criteria = 2`.
3. Còn lại, tức người lạ chưa từng liên hệ: `split_criteria = 1` (thường vào tab "Khác").

Ngoài luật trên, nội dung tin nhắn còn được chấm bằng **model AI chạy trên máy** (`core3`): `spam-categoriser` (spam) và `content-fraud-alpha-version[-za]` (lừa đảo). Model trả về `Spam score` và `Ham score`. Mẫu luật lấy từ server `message-classifier-patterns` (`core`).

### Cách làm trong `base-app` (dùng `Telephony.Sms`)
```kotlin
// Hộp thư đến
Telephony.Sms.TYPE == Telephony.Sms.MESSAGE_TYPE_INBOX   // 1
// Chưa đọc
Telephony.Sms.READ == 0
// Spam: Android không có cờ này. App tự quyết bằng:
//   số người gửi nằm trong danh sách spam/chặn, hoặc
//   người gửi không có trong danh bạ + nội dung khớp luật/model, hoặc
//   BlockedNumberContract.isBlocked(context, address)
```
Quyền cần có: `READ_SMS`, `RECEIVE_SMS`. Muốn đánh dấu đã đọc hoặc xoá tin thì app phải là **ứng dụng SMS mặc định** (`ROLE_SMS`).

---

## Quy ước khi làm việc trong repo này
- Khi trích dẫn từ mã dịch ngược, ghi rõ đường dẫn file và dòng, và đánh dấu [code] hoặc [suy luận].
- Tên class trong `defpackage/` (ví dụ `m3g`, `mw6`) là tên đã bị xáo (obfuscate) và **sẽ đổi giữa các phiên bản**. Khi tìm lại, tìm theo chuỗi SQL, đừng tìm theo tên class.
