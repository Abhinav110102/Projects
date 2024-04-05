package com.example.rxpress10.ui.settings

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.adapters.TextViewBindingAdapter.setText
import androidx.fragment.app.Fragment
import com.example.rxpress10.R
import com.example.rxpress10.databinding.ActivityEditProfileFragmentBinding
import com.example.rxpress10.databinding.ActivityPrescriptionFragmentBinding
import com.example.rxpress10.model.UserInfo
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_edit_profile_fragment.*
import kotlinx.android.synthetic.main.activity_prescription_fragment.*
import kotlinx.android.synthetic.main.activity_profile_fragment.*
import kotlinx.android.synthetic.main.medication_item.*
import java.util.*

class EditProfileFragment : Fragment(R.layout.activity_edit_profile_fragment) {

    private lateinit var binding: ActivityEditProfileFragmentBinding
    private lateinit var database: DatabaseReference
    private lateinit var  auth: FirebaseAuth
    private lateinit var userInfo: UserInfo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityEditProfileFragmentBinding.inflate(layoutInflater)

    }

    //Create a User Info Instance
    fun createUserInfo(
        firstname: String, lastName: String, email: String, dob: String,
        gender: String, pronouns: String
    ): UserInfo {
        // reads the data input on each text input and assigns it to a variable

        // creates an object named medication with the user input
        return UserInfo(
            firstname,
            lastName,
            email,
            dob,
            gender,
            pronouns
        )
    }

    //Sets values input into database
    fun database() {
        auth = FirebaseAuth.getInstance()
        var userId = auth.currentUser?.uid
        database = FirebaseDatabase.getInstance().getReference("users/" + userId + "/UserInfo")

        val firstName = binding.editFirstName.text.toString()
        val lastName = binding.editLastName.text.toString()
        val dob = binding.editDoB.text.toString()
        val gender = binding.radioGroup.checkedRadioButtonId
        val genderSelected = when (gender){
            R.id.male -> "Male"
            R.id.female -> "Female"
            R.id.nonBinary -> "Non-Binary"
            R.id.other -> "Prefer Not To Say"
            else -> "Not Specified"
        }
        val pronouns = binding.editTextTextPersonName3.text.toString()

        //Goes through all options of what could be edited and then calls the createUserInfo
        //And sets the values into the database
        if(firstName.isNotEmpty()) {
            if (lastName.isNotEmpty()) {
                if (dob.isNotEmpty()) {
                    if (genderSelected != "Not Specified") {
                        if (pronouns.isNotEmpty()) {
                            database.get().addOnSuccessListener {
                                val info = it.childrenCount.toInt()
                                userInfo = createUserInfo(
                                    firstName,
                                    lastName,
                                    it.child("email").value.toString(),
                                    dob,
                                    genderSelected,
                                    pronouns
                                )
                                database.setValue(userInfo).addOnSuccessListener {

                                    Toast.makeText(
                                        context,
                                        "Successfully Saved",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }.addOnFailureListener {
                                    Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show()
                                }
                            }
                            } else {
                                database.get().addOnSuccessListener {
                                    val info = it.childrenCount.toInt()
                                    userInfo = createUserInfo(
                                        firstName,
                                        lastName,
                                        it.child("email").value.toString(),
                                        dob,
                                        genderSelected,
                                        it.child("pronouns").value.toString()
                                    )
                                    database.setValue(userInfo).addOnSuccessListener {

                                        Toast.makeText(
                                            context,
                                            "Successfully Saved",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }.addOnFailureListener {
                                        Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show()
                                    }
                                }.addOnFailureListener {
                                    Log.e("firebase", "Error getting data", it)
                                }
                            }
                    } else {
                        if (pronouns.isNotEmpty()) {
                            database.get().addOnSuccessListener {
                                val info = it.childrenCount.toInt()
                                userInfo = createUserInfo(
                                    firstName,
                                    lastName,
                                    it.child("email").value.toString(),
                                    dob,
                                    it.child("gender").value.toString(),
                                    pronouns
                                )
                                database.setValue(userInfo).addOnSuccessListener {

                                    Toast.makeText(
                                        context,
                                        "Successfully Saved",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }.addOnFailureListener {
                                    Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show()
                                }
                            }.addOnFailureListener {
                                Log.e("firebase", "Error getting data", it)
                            }
                        } else {
                            database.get().addOnSuccessListener {
                                val info = it.childrenCount.toInt()
                                userInfo = createUserInfo(
                                    firstName,
                                    lastName,
                                    it.child("email").value.toString(),
                                    dob,
                                    it.child("gender").value.toString(),
                                    it.child("pronouns").value.toString()
                                )
                                database.setValue(userInfo).addOnSuccessListener {

                                    Toast.makeText(
                                        context,
                                        "Successfully Saved",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }.addOnFailureListener {
                                    Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show()
                                }
                            }.addOnFailureListener {
                                Log.e("firebase", "Error getting data", it)
                            }
                        }
                    }
                } else {
                    if (genderSelected != "Not Specified") {
                        if (pronouns.isNotEmpty()) {
                            database.get().addOnSuccessListener {
                                val info = it.childrenCount.toInt()
                                userInfo = createUserInfo(
                                    firstName,
                                    lastName,
                                    it.child("email").value.toString(),
                                    it.child("dob").value.toString(),
                                    genderSelected,
                                    pronouns
                                )
                                database.setValue(userInfo).addOnSuccessListener {

                                    Toast.makeText(
                                        context,
                                        "Successfully Saved",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }.addOnFailureListener {
                                    Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show()
                                }
                            }.addOnFailureListener {
                                Log.e("firebase", "Error getting data", it)
                            }
                        } else {
                            database.get().addOnSuccessListener {
                                val info = it.childrenCount.toInt()
                                userInfo = createUserInfo(
                                    firstName,
                                    lastName,
                                    it.child("email").value.toString(),
                                    it.child("dob").value.toString(),
                                    genderSelected,
                                    it.child("pronouns").value.toString()
                                )
                                database.setValue(userInfo).addOnSuccessListener {

                                    Toast.makeText(
                                        context,
                                        "Successfully Saved",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }.addOnFailureListener {
                                    Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show()
                                }
                            }.addOnFailureListener {
                                Log.e("firebase", "Error getting data", it)
                            }
                        }
                    } else {
                        if (pronouns.isNotEmpty()) {
                            database.get().addOnSuccessListener {
                                val info = it.childrenCount.toInt()
                                userInfo = createUserInfo(
                                    firstName,
                                    lastName,
                                    it.child("email").value.toString(),
                                    dob,
                                    it.child("gender").value.toString(),
                                    pronouns
                                )
                                database.setValue(userInfo).addOnSuccessListener {

                                    Toast.makeText(
                                        context,
                                        "Successfully Saved",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }.addOnFailureListener {
                                    Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show()
                                }
                            }.addOnFailureListener {
                                Log.e("firebase", "Error getting data", it)
                            }
                        } else {
                            database.get().addOnSuccessListener {
                                val info = it.childrenCount.toInt()
                                userInfo = createUserInfo(
                                    firstName,
                                    lastName,
                                    it.child("email").value.toString(),
                                    dob,
                                    it.child("gender").value.toString(),
                                    it.child("pronouns").value.toString()
                                )
                                database.setValue(userInfo).addOnSuccessListener {

                                    Toast.makeText(
                                        context,
                                        "Successfully Saved",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }.addOnFailureListener {
                                    Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show()
                                }
                            }.addOnFailureListener {
                                Log.e("firebase", "Error getting data", it)
                            }
                        }
                    }
                }
            } else {
                if (dob.isNotEmpty()) {
                    if (genderSelected != "Not Specified") {
                        if (pronouns.isNotEmpty()) {
                            database.get().addOnSuccessListener {
                                userInfo = createUserInfo(
                                    firstName, it.child("lastName").value.toString(), it.child("email").value.toString(), dob, genderSelected, pronouns
                                )
                                database.setValue(userInfo).addOnSuccessListener {

                                    Toast.makeText(
                                        context,
                                        "Successfully Saved",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }.addOnFailureListener {
                                    Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show()
                                }
                            }.addOnFailureListener {
                                Log.e("firebase", "Error getting data", it)
                            }
                        } else {
                            database.get().addOnSuccessListener {
                                val info = it.childrenCount.toInt()
                                userInfo = createUserInfo(
                                    firstName,
                                    it.child("lastName").value.toString(),
                                    it.child("email").value.toString(),
                                    dob,
                                    genderSelected,
                                    it.child("pronouns").value.toString()
                                )
                                database.setValue(userInfo).addOnSuccessListener {

                                    Toast.makeText(
                                        context,
                                        "Successfully Saved",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }.addOnFailureListener {
                                    Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show()
                                }
                            }.addOnFailureListener {
                                Log.e("firebase", "Error getting data", it)
                            }
                        }
                    } else {
                        if (pronouns.isNotEmpty()) {
                            database.get().addOnSuccessListener {
                                val info = it.childrenCount.toInt()
                                userInfo = createUserInfo(
                                    firstName,
                                    it.child("lastName").value.toString(),
                                    it.child("email").value.toString(),
                                    dob,
                                    it.child("gender").value.toString(),
                                    pronouns
                                )
                                database.setValue(userInfo).addOnSuccessListener {

                                    Toast.makeText(
                                        context,
                                        "Successfully Saved",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }.addOnFailureListener {
                                    Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show()
                                }
                            }.addOnFailureListener {
                                Log.e("firebase", "Error getting data", it)
                            }
                        } else {
                            database.get().addOnSuccessListener {
                                val info = it.childrenCount.toInt()
                                userInfo = createUserInfo(
                                    firstName,
                                    it.child("lastName").value.toString(),
                                    it.child("email").value.toString(),
                                    dob,
                                    it.child("gender").value.toString(),
                                    it.child("pronouns").value.toString()
                                )
                                database.setValue(userInfo).addOnSuccessListener {

                                    Toast.makeText(
                                        context,
                                        "Successfully Saved",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }.addOnFailureListener {
                                    Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show()
                                }
                            }.addOnFailureListener {
                                Log.e("firebase", "Error getting data", it)
                            }
                        }
                    }
                } else {
                    if (genderSelected != "Not Specified") {
                        if (pronouns.isNotEmpty()) {
                            database.get().addOnSuccessListener {
                                val info = it.childrenCount.toInt()
                                userInfo = createUserInfo(
                                    firstName,
                                    it.child("lastName").value.toString(),
                                    it.child("email").value.toString(),
                                    it.child("dob").value.toString(),
                                    genderSelected,
                                    pronouns
                                )
                                database.setValue(userInfo).addOnSuccessListener {

                                    Toast.makeText(
                                        context,
                                        "Successfully Saved",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }.addOnFailureListener {
                                    Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show()
                                }
                            }.addOnFailureListener {
                                Log.e("firebase", "Error getting data", it)
                            }
                        } else {
                            database.get().addOnSuccessListener {
                                val info = it.childrenCount.toInt()
                                userInfo = createUserInfo(
                                    firstName,
                                    it.child("lastName").value.toString(),
                                    it.child("email").value.toString(),
                                    it.child("dob").value.toString(),
                                    genderSelected,
                                    it.child("pronouns").value.toString()
                                )
                                database.setValue(userInfo).addOnSuccessListener {

                                    Toast.makeText(
                                        context,
                                        "Successfully Saved",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }.addOnFailureListener {
                                    Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show()
                                }
                            }.addOnFailureListener {
                                Log.e("firebase", "Error getting data", it)
                            }
                        }
                    } else {
                        if (pronouns.isNotEmpty()) {
                            database.get().addOnSuccessListener {
                                val info = it.childrenCount.toInt()
                                userInfo = createUserInfo(
                                    firstName,
                                    it.child("lastName").value.toString(),
                                    it.child("email").value.toString(),
                                    dob,
                                    it.child("gender").value.toString(),
                                    pronouns
                                )
                                database.setValue(userInfo).addOnSuccessListener {

                                    Toast.makeText(
                                        context,
                                        "Successfully Saved",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }.addOnFailureListener {
                                    Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show()
                                }
                            }.addOnFailureListener {
                                Log.e("firebase", "Error getting data", it)
                            }
                        } else {
                            database.get().addOnSuccessListener {
                                val info = it.childrenCount.toInt()
                                userInfo = createUserInfo(
                                    firstName,
                                    it.child("lastName").value.toString(),
                                    it.child("email").value.toString(),
                                    it.child("dob").value.toString(),
                                    it.child("gender").value.toString(),
                                    it.child("pronouns").value.toString()
                                )
                                database.setValue(userInfo).addOnSuccessListener {

                                    Toast.makeText(
                                        context,
                                        "Successfully Saved",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }.addOnFailureListener {
                                    Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show()
                                }
                            }.addOnFailureListener {
                                Log.e("firebase", "Error getting data", it)
                            }
                        }
                    }
                }
            }
        } else {
            if (lastName.isNotEmpty()) {
                if (dob.isNotEmpty()) {
                    if (genderSelected != "Not Specified") {
                        if (pronouns.isNotEmpty()) {
                            database.get().addOnSuccessListener {
                                val info = it.childrenCount.toInt()
                                userInfo = createUserInfo(
                                    it.child("firstName").value.toString(),
                                    lastName,
                                    it.child("email").value.toString(),
                                    dob,
                                    genderSelected,
                                    pronouns
                                )
                                database.setValue(userInfo).addOnSuccessListener {

                                    Toast.makeText(
                                        context,
                                        "Successfully Saved",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }.addOnFailureListener {
                                    Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show()
                                }
                            }.addOnFailureListener {
                                Log.e("firebase", "Error getting data", it)
                            }
                        } else {
                            database.get().addOnSuccessListener {
                                val info = it.childrenCount.toInt()
                                userInfo = createUserInfo(
                                    it.child("firstName").value.toString(),
                                    lastName,
                                    it.child("email").value.toString(),
                                    dob,
                                    genderSelected,
                                    it.child("pronouns").value.toString()
                                )
                                database.setValue(userInfo).addOnSuccessListener {

                                    Toast.makeText(
                                        context,
                                        "Successfully Saved",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }.addOnFailureListener {
                                    Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show()
                                }
                            }.addOnFailureListener {
                                Log.e("firebase", "Error getting data", it)
                            }
                        }
                    } else {
                        if (pronouns.isNotEmpty()) {
                            database.get().addOnSuccessListener {
                                val info = it.childrenCount.toInt()
                                userInfo = createUserInfo(
                                    it.child("firstName").value.toString(),
                                    lastName,
                                    it.child("email").value.toString(),
                                    dob,
                                    it.child("gender").value.toString(),
                                    pronouns
                                )
                                database.setValue(userInfo).addOnSuccessListener {

                                    Toast.makeText(
                                        context,
                                        "Successfully Saved",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }.addOnFailureListener {
                                    Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show()
                                }
                            }.addOnFailureListener {
                                Log.e("firebase", "Error getting data", it)
                            }
                        } else {
                            database.get().addOnSuccessListener {
                                val info = it.childrenCount.toInt()
                                userInfo = createUserInfo(
                                    it.child("firstName").value.toString(),
                                    lastName,
                                    it.child("email").value.toString(),
                                    dob,
                                    it.child("gender").value.toString(),
                                    it.child("pronouns").value.toString()
                                )
                                database.setValue(userInfo).addOnSuccessListener {

                                    Toast.makeText(
                                        context,
                                        "Successfully Saved",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }.addOnFailureListener {
                                    Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show()
                                }
                            }.addOnFailureListener {
                                Log.e("firebase", "Error getting data", it)
                            }
                        }
                    }
                } else {
                    if (genderSelected != "Not Specified") {
                        if (pronouns.isNotEmpty()) {
                            database.get().addOnSuccessListener {
                                val info = it.childrenCount.toInt()
                                userInfo = createUserInfo(
                                    it.child("firstName").value.toString(),
                                    lastName,
                                    it.child("email").value.toString(),
                                    it.child("dob").value.toString(),
                                    genderSelected,
                                    pronouns
                                )
                                database.setValue(userInfo).addOnSuccessListener {

                                    Toast.makeText(
                                        context,
                                        "Successfully Saved",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }.addOnFailureListener {
                                    Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show()
                                }
                            }.addOnFailureListener {
                                Log.e("firebase", "Error getting data", it)
                            }
                        } else {
                            database.get().addOnSuccessListener {
                                val info = it.childrenCount.toInt()
                                userInfo = createUserInfo(
                                    it.child("firstName").value.toString(),
                                    lastName,
                                    it.child("email").value.toString(),
                                    it.child("dob").value.toString(),
                                    genderSelected,
                                    it.child("pronouns").value.toString()
                                )
                                database.setValue(userInfo).addOnSuccessListener {

                                    Toast.makeText(
                                        context,
                                        "Successfully Saved",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }.addOnFailureListener {
                                    Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show()
                                }
                            }.addOnFailureListener {
                                Log.e("firebase", "Error getting data", it)
                            }
                        }
                    } else {
                        if (pronouns.isNotEmpty()) {
                            database.get().addOnSuccessListener {
                                val info = it.childrenCount.toInt()
                                userInfo = createUserInfo(
                                    it.child("firstName").value.toString(),
                                    lastName,
                                    it.child("email").value.toString(),
                                    it.child("dob").value.toString(),
                                    it.child("gender").value.toString(),
                                    pronouns
                                )
                                database.setValue(userInfo).addOnSuccessListener {

                                    Toast.makeText(
                                        context,
                                        "Successfully Saved",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }.addOnFailureListener {
                                    Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show()
                                }
                            }.addOnFailureListener {
                                Log.e("firebase", "Error getting data", it)
                            }
                        } else {
                            database.get().addOnSuccessListener {
                                val info = it.childrenCount.toInt()
                                userInfo = createUserInfo(
                                    it.child("firstName").value.toString(),
                                    lastName,
                                    it.child("email").value.toString(),
                                    it.child("dob").value.toString(),
                                    it.child("gender").value.toString(),
                                    it.child("pronouns").value.toString()
                                )
                                database.setValue(userInfo).addOnSuccessListener {

                                    Toast.makeText(
                                        context,
                                        "Successfully Saved",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }.addOnFailureListener {
                                    Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show()
                                }
                            }.addOnFailureListener {
                                Log.e("firebase", "Error getting data", it)
                            }
                        }
                    }
                }
            } else {
                if (dob.isNotEmpty()){
                    if (genderSelected != "Not Specified") {
                        if (pronouns.isNotEmpty()) {
                            database.get().addOnSuccessListener {
                                val info = it.childrenCount.toInt()
                                userInfo = createUserInfo(
                                    it.child("firstName").value.toString(),
                                    it.child("lastName").value.toString(),
                                    it.child("email").value.toString(),
                                    dob,
                                    genderSelected,
                                    pronouns
                                )
                                database.setValue(userInfo).addOnSuccessListener {

                                    Toast.makeText(
                                        context,
                                        "Successfully Saved",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }.addOnFailureListener {
                                    Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show()
                                }
                            }.addOnFailureListener {
                                Log.e("firebase", "Error getting data", it)
                            }
                        } else {
                            database.get().addOnSuccessListener {
                                val info = it.childrenCount.toInt()
                                userInfo = createUserInfo(
                                    it.child("firstName").value.toString(),
                                    it.child("lastName").value.toString(),
                                    it.child("email").value.toString(),
                                    dob,
                                    genderSelected,
                                    it.child("pronouns").value.toString()
                                )
                                database.setValue(userInfo).addOnSuccessListener {

                                    Toast.makeText(
                                        context,
                                        "Successfully Saved",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }.addOnFailureListener {
                                    Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show()
                                }
                            }.addOnFailureListener {
                                Log.e("firebase", "Error getting data", it)
                            }
                        }
                    } else {
                        if (pronouns.isNotEmpty()) {
                            database.get().addOnSuccessListener {
                                val info = it.childrenCount.toInt()
                                userInfo = createUserInfo(
                                    it.child("firstName").value.toString(),
                                    it.child("lastName").value.toString(),
                                    it.child("email").value.toString(),
                                    dob,
                                    it.child("gender").value.toString(),
                                    pronouns
                                )
                                database.setValue(userInfo).addOnSuccessListener {

                                    Toast.makeText(
                                        context,
                                        "Successfully Saved",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }.addOnFailureListener {
                                    Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show()
                                }
                            }.addOnFailureListener {
                                Log.e("firebase", "Error getting data", it)
                            }
                        } else {
                            database.get().addOnSuccessListener {
                                val info = it.childrenCount.toInt()
                                userInfo = createUserInfo(
                                    it.child("firstName").value.toString(),
                                    it.child("lastName").value.toString(),
                                    it.child("email").value.toString(),
                                    dob,
                                    it.child("gender").value.toString(),
                                    it.child("pronouns").value.toString()
                                )
                                database.setValue(userInfo).addOnSuccessListener {

                                    Toast.makeText(
                                        context,
                                        "Successfully Saved",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }.addOnFailureListener {
                                    Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show()
                                }
                            }.addOnFailureListener {
                                Log.e("firebase", "Error getting data", it)
                            }
                        }
                    }
                } else {
                    if (genderSelected != "Not Specified") {
                        if (pronouns.isNotEmpty()) {
                            database.get().addOnSuccessListener {
                                val info = it.childrenCount.toInt()
                                userInfo = createUserInfo(
                                    it.child("firstName").value.toString(),
                                    it.child("lastName").value.toString(),
                                    it.child("email").value.toString(),
                                    it.child("dob").value.toString(),
                                    genderSelected,
                                    pronouns
                                )
                                database.setValue(userInfo).addOnSuccessListener {

                                    Toast.makeText(
                                        context,
                                        "Successfully Saved",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }.addOnFailureListener {
                                    Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show()
                                }
                            }.addOnFailureListener {
                                Log.e("firebase", "Error getting data", it)
                            }
                        } else {
                            database.get().addOnSuccessListener {
                                val info = it.childrenCount.toInt()
                                userInfo = createUserInfo(
                                    it.child("firstName").value.toString(),
                                    it.child("lastName").value.toString(),
                                    it.child("email").value.toString(),
                                    it.child("dob").value.toString(),
                                    genderSelected,
                                    it.child("pronouns").value.toString()
                                )
                                database.setValue(userInfo).addOnSuccessListener {

                                    Toast.makeText(
                                        context,
                                        "Successfully Saved",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }.addOnFailureListener {
                                    Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show()
                                }
                            }.addOnFailureListener {
                                Log.e("firebase", "Error getting data", it)
                            }
                        }
                    } else {
                        if (pronouns.isNotEmpty()) {
                            database.get().addOnSuccessListener {
                                val info = it.childrenCount.toInt()
                                userInfo = createUserInfo(
                                    it.child("firstName").value.toString(),
                                    it.child("lastName").value.toString(),
                                    it.child("email").value.toString(),
                                    it.child("dob").value.toString(),
                                    it.child("gender").value.toString(),
                                    pronouns
                                )
                                database.setValue(userInfo).addOnSuccessListener {

                                    Toast.makeText(
                                        context,
                                        "Successfully Saved",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }.addOnFailureListener {
                                    Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show()
                                }
                            }.addOnFailureListener {
                                Log.e("firebase", "Error getting data", it)
                            }
                        } else {
                            database.get().addOnSuccessListener {
                                val info = it.childrenCount.toInt()
                                userInfo = createUserInfo(
                                    it.child("firstName").value.toString(),
                                    it.child("lastName").value.toString(),
                                    it.child("email").value.toString(),
                                    it.child("dob").value.toString(),
                                    it.child("gender").value.toString(),
                                    it.child("pronouns").value.toString()
                                )
                                database.setValue(userInfo).addOnSuccessListener {

                                    Toast.makeText(
                                        context,
                                        "Successfully Saved",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }.addOnFailureListener {
                                    Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show()
                                }
                            }.addOnFailureListener {
                                Log.e("firebase", "Error getting data", it)
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        //Gets the current day, month, and year
        val calen = Calendar.getInstance()
        val currentYear = calen.get(Calendar.YEAR)
        val currentmo = calen.get(Calendar.MONTH)
        val currentday = calen.get(Calendar.DAY_OF_MONTH)
        //Listens for the save button to be clicked and then calls the database function
        binding.saveBtn.setOnClickListener { database() }
        //Listens for a click on the edit dob text box and then produces a datepicker
        binding.editDoB.setOnClickListener {
            val cal = Calendar.getInstance()
            val year = cal.get(Calendar.YEAR)
            val mo = cal.get(Calendar.MONTH)
            val day = cal.get(Calendar.DAY_OF_MONTH)
            val datePickerDialog = DatePickerDialog(
                    // on below line we are passing context.
                    this.requireContext(),
                    { view, year, monthOfYear, dayOfMonth ->
                        // on below line we are setting
                        // date to our edit text.
                        val dat = ((monthOfYear+1).toString() + "-" + dayOfMonth.toString() + "-" + year)
                        //Makes sure the year is not less than 1900
                        //Makes sure the year is not greater than the current year
                        //Makes sure that if the year is the current year that the month is not greater than the current month
                        //Makes sure that if the year is the current year and the month is the current month that the day is not greater than the current day
                        if (year < 1900) {
                            Toast.makeText(this.context, "Year cannot be less than 1900", Toast.LENGTH_SHORT).show()
                        } else if (year > currentYear) {
                            Toast.makeText(this.context, "Year cannot exceed the current year", Toast.LENGTH_SHORT).show()
                        } else if (year == currentYear && (monthOfYear+1) > (currentmo+1)) {
                            Toast.makeText(this.context, "Month cannot exceed the current month in the current year", Toast.LENGTH_SHORT).show()
                        } else if (year == currentYear && (monthOfYear+1) == (currentmo+1) && dayOfMonth > currentday) {
                            Toast.makeText(this.context, "Day cannot exceed the current day in the current month in the current year", Toast.LENGTH_SHORT).show()
                        } else {
                            binding.editDoB.setText(dat)
                        }
                    },
                    // on below line we are passing year, month
                    // and day for the selected date in our date picker.
                    year,
                    mo,
                    day
                )
            // at last we are calling show
            // to display our date picker dialog.
            datePickerDialog.show()
        }

        return binding.root

    }
}