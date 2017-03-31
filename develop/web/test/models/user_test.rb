require 'test_helper'

class UserTest < ActiveSupport::TestCase
  # test "the truth" do
  #   assert true
  # end
  def setup
  	@user = User.new(name:"Exapmle User", email:"user@example.com", phone:"123456789", key:"Key", password:"Default")
  end

  test "should be valid" do
  	assert @user.valid?
  end

  test "name should be present" do
  	@user.name = ""
  	assert_not @user.valid?
  end

  test "name should be unique" do
  	duplicate_user = @user.dup
  	@user.save
  	duplicate_user.name = "1223312"
  	assert_not duplicate_user.valid?
  end

  test "name should be saved as lower-case" do
    mixed_case_name = "   Foo123  "
    @user.name = mixed_case_name
    @user.save
    print @user.reload.name
    assert_equal mixed_case_name.downcase.strip, @user.reload.name
  end

  test "name shoule not be too short" do
  	@user.name = "a"*5
  	assert_not @user.valid?
  end

  test "name shoule not be too long" do
  	@user.name = "a"*21
  	assert_not @user.valid?
  end

  test "phone should be present" do
  	@user.phone = ""
  	assert_not @user.valid?
  end

  test "phone shoule not be too short" do
  	@user.phone = "a"*2
  	assert_not @user.valid?
  end

  test "phone shoule not be too long" do
  	@user.phone = "a"*21
  	assert_not @user.valid?
  end

  test "email be valid" do
  	@user.email = "123ss.com"
  	assert_not @user.valid?
  end

  test "email addresses should be saved as lower-case" do
    mixed_case_email = "Foo@ExAMPle.CoM"
    @user.email = mixed_case_email
    @user.save
    assert_equal mixed_case_email.downcase, @user.reload.email
  end

  test "password should be present" do
  	@user.password = "1234"
  	assert_not @user.valid?
  end

  test "password shoule not be too short" do
  	@user.password = "a"*5
  	assert_not @user.valid?
  end

  test "password shoule not be too long" do
  	@user.password = "a"*257
  	assert_not @user.valid?
  end

end
