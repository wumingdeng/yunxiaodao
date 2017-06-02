'use strict';
module.exports = function(sequelize, DataTypes) {
  var yxd_parameters = sequelize.define('yxd_parameters', {
    mac_id: DataTypes.STRING,
    left_length:DataTypes.FLOAT,
    left_width:DataTypes.FLOAT,
    left_width_41full:DataTypes.FLOAT,
    left_center_angle:DataTypes.FLOAT,
    left_length_90:DataTypes.FLOAT,
    left_length_825:DataTypes.FLOAT,
    left_length_78:DataTypes.FLOAT,
    left_length_725:DataTypes.FLOAT,
    left_length_635:DataTypes.FLOAT,
    left_length_68:DataTypes.FLOAT,
    left_length_41:DataTypes.FLOAT,
    left_length_18:DataTypes.FLOAT,
    left_width_90:DataTypes.FLOAT,
    left_width_78:DataTypes.FLOAT,
    left_width_725:DataTypes.FLOAT,
    left_width_635:DataTypes.FLOAT,
    left_width_41:DataTypes.FLOAT,
    left_width_18:DataTypes.FLOAT,
    left_width_ratio:DataTypes.FLOAT,
    left_width_68:DataTypes.FLOAT,
    left_incline_angle:DataTypes.FLOAT,
    left_size_01:DataTypes.FLOAT,
    left_size_02:DataTypes.FLOAT,
    left_size_03:DataTypes.FLOAT,
    left_size_04:DataTypes.FLOAT,
    left_size_05:DataTypes.FLOAT,
    left_size_06:DataTypes.FLOAT,
    left_size_07:DataTypes.FLOAT,
    left_size_08:DataTypes.FLOAT,
    left_size_09:DataTypes.FLOAT,
    left_size_10:DataTypes.FLOAT,
    left_size_11:DataTypes.FLOAT,
    left_size_12:DataTypes.FLOAT,
    left_size_13:DataTypes.FLOAT,
    right_length:DataTypes.FLOAT,
    right_width:DataTypes.FLOAT,
    right_width_41full:DataTypes.FLOAT,
    right_center_angle:DataTypes.FLOAT,
    right_length_90:DataTypes.FLOAT,
    right_length_825:DataTypes.FLOAT,
    right_length_78:DataTypes.FLOAT,
    right_length_725:DataTypes.FLOAT,
    right_length_635:DataTypes.FLOAT,
    right_length_68:DataTypes.FLOAT,
    right_length_41:DataTypes.FLOAT,
    right_length_18:DataTypes.FLOAT,
    right_width_90:DataTypes.FLOAT,
    right_width_78:DataTypes.FLOAT,
    right_width_725:DataTypes.FLOAT,
    right_width_635:DataTypes.FLOAT,
    right_width_41:DataTypes.FLOAT,
    right_width_18:DataTypes.FLOAT,
    right_width_ratio:DataTypes.FLOAT,
    right_width_68:DataTypes.FLOAT,
    right_incline_angle:DataTypes.FLOAT,
    right_size_01:DataTypes.FLOAT,
    right_size_02:DataTypes.FLOAT,
    right_size_03:DataTypes.FLOAT,
    right_size_04:DataTypes.FLOAT,
    right_size_05:DataTypes.FLOAT,
    right_size_06:DataTypes.FLOAT,
    right_size_07:DataTypes.FLOAT,
    right_size_08:DataTypes.FLOAT,
    right_size_09:DataTypes.FLOAT,
    right_size_10:DataTypes.FLOAT,
    right_size_11:DataTypes.FLOAT,
    right_size_12:DataTypes.FLOAT,
    right_size_13:DataTypes.FLOAT
  },{
       timestamps: false
  }, {
    classMethods: {
      associate: function(models) {
        // associations can be defined here
      }
    }
  });
  return yxd_parameters;
};