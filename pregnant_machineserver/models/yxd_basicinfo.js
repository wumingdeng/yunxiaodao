'use strict';
module.exports = function(sequelize, DataTypes) {
  var yxd_basicinfos = sequelize.define('yxd_basicinfos', {
    mac_id: DataTypes.STRING,
    user_id: DataTypes.INTEGER,//
    open_id: DataTypes.STRING,//
    card_id:DataTypes.STRING,//
    scene:DataTypes.STRING,
    ticket:DataTypes.STRING,//
    from_app:DataTypes.STRING,
    from_id:DataTypes.STRING,//
    analyse:DataTypes.INTEGER,
    name:DataTypes.STRING,
    birth:DataTypes.STRING,
    age:DataTypes.INTEGER,//
    sex:DataTypes.STRING,
    district:DataTypes.STRING,
    province:DataTypes.STRING,
    city:DataTypes.STRING,
    county:DataTypes.STRING,//
    country:DataTypes.STRING,
    nation:DataTypes.STRING,
    phone:DataTypes.STRING,
    email:DataTypes.STRING,
    date_server:DataTypes.BIGINT,
    date_host:DataTypes.BIGINT,
    sign:DataTypes.STRING,
    crowd:DataTypes.STRING,
    period:DataTypes.INTEGER,//
    date_yunfu:DataTypes.BIGINT,//
    remark:DataTypes.STRING,
    height:DataTypes.FLOAT,
    weight:DataTypes.FLOAT,
    blood:DataTypes.STRING,
    hospital_no:DataTypes.STRING,
    hospital_name:DataTypes.STRING,//
    doctor_name:DataTypes.STRING,
    doctor_id:DataTypes.INTEGER,
    status:DataTypes.INTEGER,
    view_type:DataTypes.INTEGER
  },{
       timestamps: false
  }, {
    classMethods: {
      associate: function(models) {
        // associations can be defined here
      }
    }
  });
  return yxd_basicinfos;
};