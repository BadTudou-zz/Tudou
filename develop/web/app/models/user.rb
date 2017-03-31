class User < ApplicationRecord
	VALID_EMAIL_REGEX = /\A[\w+\-.]+@[a-z\d\-.]+\.[a-z]+\z/i
	before_save { self.name = name.downcase.strip }
	before_save { self.phone = phone.strip }
	before_save { self.email = email.downcase.strip }
	before_save { self.password = password.strip }
	validates :name, presence: true, uniqueness:{ case_sensitive: false }, length:{ minimum:6, maximum:20}
	validates :phone, presence: true, uniqueness:true, length:{ minimum:3, maximum:20}
	validates :email, presence: false, uniqueness:true, format:{ with: VALID_EMAIL_REGEX}
	validates :password, presence: true, length:{ minimum:6, maximum:256}
	has_secure_password 
end
