package com.motorcycleparts.motorcycleparts_master;

import com.motorcycleparts.motorcycleparts_master.auth.AuthenticationService;
import com.motorcycleparts.motorcycleparts_master.auth.RegisterRequest;
import com.motorcycleparts.motorcycleparts_master.model.*;
import com.motorcycleparts.motorcycleparts_master.model.user.Role;

import com.motorcycleparts.motorcycleparts_master.repository.*;
import com.motorcycleparts.motorcycleparts_master.token.Token;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.text.Normalizer;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

@SpringBootApplication
@EnableScheduling
public class MotorcyclePartsMainClass {

	public static void main(String[] args) {
		SpringApplication.run(MotorcyclePartsMainClass.class, args);
	}

	@Bean
	public CommandLineRunner commandLineRunner(
			AuthenticationService authenticationService,
			SparePartsTypeRepository sparePartsTypeRepository,
			BrandPartsRepository brandPartsRepository,
			SparePartsRepository sparePartsRepository,
			MotorTypeRepository motorTypeRepository,
			BrandMotorRepository brandMotorRepository,
			Parts_MotorTypeRepository parts_motorTypeRepository,
			ReviewsRepository reviewsRepository,
			CustomerRepository customerRepository,
			DiscountRepository discountRepository,
			TokenRepository tokenRepository
	) {
		return args -> {
			var admin = RegisterRequest.builder()
					.firstName("admin")
					.lastName("admin")
					.email("admin@admin.com")
					.password("admin")
					.role(Role.ADMIN).build();
			System.out.println("Admin token: " + authenticationService.register(admin).getAccess_token());

//		var customer = RegisterRequest.builder()
//				.firstName("user")
//				.lastName("user")
//				.email("huynhname32@gmail.com")
//				.password("1")
//				.build();
//		authenticationService.register(customer);

//		// Mảng nội dung đánh giá tích cực
//		String[] reviewContents = {"Sản phẩm chất lượng, giá cả hợp lý", "Rất hài lòng với sản phẩm", "Sản phẩm tuyệt vời, sẽ mua lại", "Giá trị tốt cho tiền", "Chất lượng sản phẩm vượt trội", "Sản phẩm đáng mua", "Rất hài lòng với dịch vụ", "Giao hàng nhanh chóng", "Sản phẩm đúng như mô tả", "Sẽ giới thiệu cho bạn bè"};
//
//// Mảng tên đầu
//		String[] firstName = {"Nguyễn", "Trần", "Lê", "Phạm", "Hoàng", "Huỳnh", "Võ", "Đặng", "Bùi", "Đỗ", "Hồ", "Ngô", "Dương", "Lý", "Đào", "Đinh"};
//// Mảng tên cuối
//		String[] lastName = {"Văn", "Trung", "Hồng", "Hải", "Lan", "Huy", "Hùng", "Hưng", "Hoa", "Hạnh", "Hương", "Huyền", "Thắm", "Trinh", "Ngọc", "Thảo"};
//
//		Random rand = new Random();
//		Set<String> usedEmails = new HashSet<>();
//
//		for (int i = 0; i < 200; i++) {
//			String first = firstName[rand.nextInt(firstName.length)];
//			String last = lastName[rand.nextInt(lastName.length)];
//			String email = first + last + "@gmail.com";
//
//			// Kiểm tra xem email đã được sử dụng chưa
//			while (usedEmails.contains(email)) {
//				first = firstName[rand.nextInt(firstName.length)];
//				last = lastName[rand.nextInt(lastName.length)];
//				email = first + last + "@gmail.com";
//			}
//
//			// Thêm email vào tập hợp các email đã sử dụng
//			usedEmails.add(email);
//
//			// Tạo số điện thoại ngẫu nhiên gồm 10 chữ số
//			// Tạo số điện thoại ngẫu nhiên gồm 10 chữ số, bắt đầu bằng số 0
//			String phoneNumber = "0" + String.format("%09d", rand.nextInt(1_000_000_000));
//
//
//			var customer = RegisterRequest.builder()
//					.firstName(first)
//					.lastName(last)
//					.email(email)
//					.sex("Male")
//					.phoneNumber(phoneNumber)
//					.password("1")
//					.build();
//			authenticationService.register(customer);
//
//			// Tạo 10 đánh giá cho mỗi người dùng
//			Set<Long> reviewedProducts = new HashSet<>();
//			for (int j = 0; j < 10; j++) {
//				long productId;
//
//				// Chọn sản phẩm ngẫu nhiên mà người dùng chưa đánh giá
//				do {
//					productId = rand.nextInt(37) + 1;
//				} while (reviewedProducts.contains(productId));
//
//				// Thêm sản phẩm vào tập hợp các sản phẩm đã đánh giá
//				reviewedProducts.add(productId);
//
//				// Tạo đánh giá ngẫu nhiên
//				String content = reviewContents[rand.nextInt(reviewContents.length)];
//				int rating = rand.nextInt(5) + 1; // Tạo số sao ngẫu nhiên từ 1 đến 5
//				Reviews reviews = Reviews.builder()
//						.content(content)
//						.rating(rating)
//						.customer(customerRepository.findById((long) i+1).get())
//						.date(null) // Sử dụng ngày hiện tại
//						.spareParts(sparePartsRepository.findById(productId).get())
//						.build(); // Thêm build() để tạo đối tượng từ builder
//				reviewsRepository.save(reviews); // Lưu đối tượng vào cơ sở dữ liệu
//			}
//		}

//		// Mảng nội dung đánh giá tích cực
//		String[] reviewContents = {"Sản phẩm chất lượng, giá cả hợp lý", "Rất hài lòng với sản phẩm", "Sản phẩm tuyệt vời, sẽ mua lại", "Giá trị tốt cho tiền", "Chất lượng sản phẩm vượt trội", "Sản phẩm đáng mua", "Rất hài lòng với dịch vụ", "Giao hàng nhanh chóng", "Sản phẩm đúng như mô tả", "Sẽ giới thiệu cho bạn bè"};
//
//		// Mảng tên đầu
//		String[] firstName = {"Nguyễn", "Trần", "Lê", "Phạm", "Hoàng", "Huỳnh", "Võ", "Đặng", "Bùi", "Đỗ", "Hồ", "Ngô", "Dương", "Lý", "Đào", "Đinh"};
//		// Mảng tên cuối
//		String[] lastName = {"Văn", "Trung", "Hồng", "Hải", "Lan", "Huy", "Hùng", "Hưng", "Hoa", "Hạnh", "Hương", "Huyền", "Thắm", "Trinh", "Ngọc", "Thảo"};
//
//		Random rand = new Random();
//		Set<String> usedEmails = new HashSet<>();
//
//		for (int i = 0; i < 200; i++) {
//			String first = firstName[rand.nextInt(firstName.length)];
//			String last = lastName[rand.nextInt(lastName.length)];
//
//			// Loại bỏ dấu tiếng Việt và chuyển đổi thành chữ thường
//			String email = removeAccent(first.toLowerCase()) + removeAccent(last.toLowerCase()) + "@gmail.com";
//
//			// Kiểm tra xem email đã được sử dụng chưa
//			while (usedEmails.contains(email)) {
//				first = firstName[rand.nextInt(firstName.length)];
//				last = lastName[rand.nextInt(lastName.length)];
//				email = removeAccent(first.toLowerCase()) + removeAccent(last.toLowerCase()) + "@gmail.com";
//			}
//
//			// Thêm email vào tập hợp các email đã sử dụng
//			usedEmails.add(email);
//
//			// Tạo số điện thoại ngẫu nhiên gồm 10 chữ số
//			// Tạo số điện thoại ngẫu nhiên gồm 10 chữ số, bắt đầu bằng số 0
//			String phoneNumber = "0" + String.format("%09d", rand.nextInt(1_000_000_000));
//
//
//			var customer = RegisterRequest.builder()
//					.firstName(first)
//					.lastName(last)
//					.email(email.toLowerCase()) // Viết email thành chữ thường
//					.sex("male") // Giới tính viết thường
//					.phoneNumber(phoneNumber)
//					.password("1")
//					.build();
//			authenticationService.register(customer);
//
//			// Tạo 10 đánh giá cho mỗi người dùng
//			Set<Long> reviewedProducts = new HashSet<>();
//			for (int j = 0; j < 10; j++) {
//				long productId;
//
//				// Chọn sản phẩm ngẫu nhiên mà người dùng chưa đánh giá
//				do {
//					productId = rand.nextInt(37) + 1;
//				} while (reviewedProducts.contains(productId));
//
//				// Thêm sản phẩm vào tập hợp các sản phẩm đã đánh giá
//				reviewedProducts.add(productId);
//
//				// Tạo đánh giá ngẫu nhiên
//				String content = reviewContents[rand.nextInt(reviewContents.length)];
//				int rating = rand.nextInt(5) + 1; // Tạo số sao ngẫu nhiên từ 1 đến 5
//				Date currentDate = new Date(); // Lấy ngày hiện tại
//
//				Reviews reviews = Reviews.builder()
//						.content(content)
//						.rating(rating)
//						.customer(customerRepository.findById((long) i + 1).get())
//						.date(currentDate) // Sử dụng ngày hiện tại
//						.spareParts(sparePartsRepository.findById(productId).get())
//						.build();
//				reviewsRepository.save(reviews); // Lưu đối tượng vào cơ sở dữ liệu
//			}
//		}
//	};
//	}
//
//	// Hàm loại bỏ dấu tiếng Việt
//	public static String removeAccent(String s) {
//		String temp = Normalizer.normalize(s, Normalizer.Form.NFD);
//		return temp.replaceAll("[^\\p{ASCII}]", "");
//	}


//			for(int i = 1; i <= 20; i++) {
//				Reviews reviews = Reviews.builder()
//						.content("Sản phẩm chất lượng, giá cả hợp lý")
//						.rating(5)
//						.customer(customerRepository.findById(2L).get())
//						.date(null) // Sử dụng ngày hiện tại
//						.spareParts(sparePartsRepository.findById(2L).get())
//						.build(); // Thêm build() để tạo đối tượng từ builder
//				reviewsRepository.save(reviews); // Lưu đối tượng vào cơ sở dữ liệu
//			}


		};
	}
	}


