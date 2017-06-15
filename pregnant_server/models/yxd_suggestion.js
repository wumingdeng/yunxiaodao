'use strict';
module.exports = function(sequelize, DataTypes) {
  var yxd_suggestions = sequelize.define('yxd_suggestions', {
    mac_id: DataTypes.STRING,
    left_foot_size:DataTypes.STRING,
    left_foot_width:DataTypes.STRING,
    left_foot_width2:DataTypes.STRING,
    left_foot_status:DataTypes.STRING,
    left_advise:DataTypes.STRING,
    right_foot_size:DataTypes.STRING,
    right_foot_width:DataTypes.STRING,
    right_foot_width2:DataTypes.STRING,
    right_foot_status:DataTypes.STRING,
    right_advise:DataTypes.STRING,
    left_para_01:DataTypes.FLOAT,
    left_para_02:DataTypes.FLOAT,
    left_para_03:DataTypes.FLOAT,
    left_para_04:DataTypes.FLOAT,
    left_para_05:DataTypes.FLOAT,
    left_para_06:DataTypes.FLOAT,
    right_para_01:DataTypes.FLOAT,
    right_para_02:DataTypes.FLOAT,
    right_para_03:DataTypes.FLOAT,
    right_para_04:DataTypes.FLOAT,
    right_para_05:DataTypes.FLOAT,
    right_para_06:DataTypes.FLOAT,
    left_foot_type:DataTypes.STRING,
    right_foot_type:DataTypes.STRING,
    foot_leg:DataTypes.STRING,
    left_int_size:DataTypes.INTEGER,
    left_int_width:DataTypes.INTEGER,
    left_int_width2:DataTypes.INTEGER,
    left_int_status:DataTypes.INTEGER,
    right_int_size:DataTypes.INTEGER,
    right_int_width:DataTypes.INTEGER,
    right_int_width2:DataTypes.INTEGER,
    right_int_status:DataTypes.INTEGER,
    left_int_type:DataTypes.INTEGER,
    right_int_type:DataTypes.INTEGER,
    foot_int_leg:DataTypes.INTEGER
  },{
       timestamps: false
  }, {
    classMethods: {
      associate: function(models) {
        // associations can be defined here
      }
    }
  });
  return yxd_suggestions;
};