'use strict';
module.exports = function(sequelize, DataTypes) {
  var yxd_details = sequelize.define('yxd_details', {
    hospital_no: DataTypes.STRING,
    clinic_dept: DataTypes.INTEGER,//
    doctor_name: DataTypes.STRING,//
    clinic_type:DataTypes.STRING,//
    count_num:DataTypes.INTEGER,
    open_id:DataTypes.STRING,//
    card_id:DataTypes.STRING,
    pat_name:DataTypes.STRING,//
    print_date:DataTypes.INTEGER,
    call_times:DataTypes.INTEGER,
    start_date:DataTypes.BIGINT,
    end_date:DataTypes.BIGINT,//
    status:DataTypes.STRING,
    repeat_num:DataTypes.INTEGER,
    record_id:DataTypes.INTEGER
  },{
       timestamps: false
  }, {
    classMethods: {
      associate: function(models) {
        // associations can be defined here
      }
    }
  });
  return yxd_details;
};